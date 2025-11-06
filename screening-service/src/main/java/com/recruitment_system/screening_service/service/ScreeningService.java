package com.recruitment_system.screening_service.service;

import com.recruitment_system.dto.JobPostResponseDto;
import com.recruitment_system.event.ConfirmationEmailEvent;
import com.recruitment_system.event.PostDeadlineEvent;
import com.recruitment_system.event.SaveScreeningResultEvent;
import com.recruitment_system.screening_service.dto.ApplicationResponseDto;
import com.recruitment_system.screening_service.dto.EmailContent;
import com.recruitment_system.screening_service.dto.ScreeningResultDto;
import com.recruitment_system.screening_service.feign.JobPostInterface;
import com.recruitment_system.screening_service.feign.ApplicationInterface;
import com.recruitment_system.screening_service.model.EmailRequest;
import com.recruitment_system.screening_service.model.EmailStatus;
import com.recruitment_system.screening_service.model.ScreeningResult;
import com.recruitment_system.screening_service.repository.EmailRequestRepository;
import com.recruitment_system.screening_service.repository.ScreeningResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreeningService {
    private final ApplicationInterface applicationInterface;
    private final JobPostInterface jobPostInterface;
    private final AiScreeningClient aiScreeningClient;
    private final ScreeningResultRepository resultRepository;
    private final EmailRequestRepository emailRequestRepository;
    private final EventProducer eventProducer;





    public List<ScreeningResultDto> runScreeningManually(Long postId) {
        try {
            System.out.println("Running manual screening for job post ID: " + postId);
            return processScreening(postId);
        } catch (Exception ex) {
            System.err.println("Error processing manual screening for job post ID " +
                    postId + ": " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    List<ScreeningResultDto> processScreening(Long jobPostId) {
        var jobPost = jobPostInterface.getJobPostById(jobPostId).getBody().getData();
        var resumes = applicationInterface.getAllByPostId(jobPostId).getBody().getData();

        List<ScreeningResultDto> aiResults = aiScreeningClient.screenedResumes(jobPost, resumes);
        List<ScreeningResult> results = new ArrayList<>();

        for (ScreeningResultDto aiResult : aiResults) {
            ScreeningResult result = ScreeningResult.builder()
                    .cv_summary(aiResult.getCv_summary())
                    .github_summary(aiResult.getGithub_summary())
                    .candidate_name(aiResult.getCandidate_name())
                    .email(aiResult.getEmail())
                    .score(aiResult.getScore())
                    .jobPostId(jobPostId)
                    .match_analysis(aiResult.getMatch_analysis())
                    .build();

            resultRepository.save(result);
            log.info("Saved screening result for candidate: " + result.getCandidate_name());
            eventProducer.sendSaveScreeningResultEvent(new SaveScreeningResultEvent(
                    jobPostId,
                    result.getEmail(),
                    result.getScore()));
            results.add(result);
        }

        return aiResults;
    }
    public List<ScreeningResultDto> getRankedList(Long jobPostId) {
        List<ScreeningResult> results = resultRepository.findByJobPostIdOrderByScoreDesc(jobPostId);
        List<ScreeningResultDto> dtoResults = new ArrayList<>();
        for(ScreeningResult result : results){
            ScreeningResultDto dto = ScreeningResultDto.builder()
                    .cv_summary(result.getCv_summary())
                    .github_summary(result.getGithub_summary())
                    .candidate_name(result.getCandidate_name())
                    .email(result.getEmail())
                    .match_analysis(result.getMatch_analysis())
                    .score(result.getScore())
                    .build();
            dtoResults.add(dto);
        }
        return dtoResults;
    }
    @Transactional
    public void confirmApplicants(Long jobPostId, List<String> selectedApplicationEmails) {
        JobPostResponseDto jobPost =
                jobPostInterface.getJobPostById(jobPostId).getBody().getData();

        List<ApplicationResponseDto> applications =
                applicationInterface.getAllByPostId(jobPostId).getBody().getData()
                .stream()
                .filter(a -> selectedApplicationEmails.contains(a.getUserEmail()))
                .collect(Collectors.toList());

        // ask AI to generate personalized subject & body for each applicant
        Map<String, EmailContent> emailContents =
                aiScreeningClient.generateConfirmationEmails(jobPost, applications);

        for (ApplicationResponseDto app : applications) {
            EmailContent content = emailContents.get(app.getUserEmail());
            String correlationId = UUID.randomUUID().toString();

            // persist request (optional, for status tracking)
            EmailRequest emailRequest = EmailRequest.builder()
                    .applicationId(app.getApplicationId())
                    .jobPostId(app.getPostId())
                    .email(app.getUserEmail())
                    .subject(content.getSubject())
                    .body(content.getBody())
                    .status(EmailStatus.PENDING)
                    .correlationId(correlationId)
                    .createdAt(LocalDateTime.now())
                    .build();
            emailRequestRepository.save(emailRequest);

            // publish event to Kafka
            ConfirmationEmailEvent event = new ConfirmationEmailEvent(
                    app.getApplicationId(),
                    app.getPostId(),
                    app.getUserEmail(),
                    content.getSubject(),
                    content.getBody()
            );
            eventProducer.sendConfirmationEmailEvent(event);
        }

    }
}

