package com.recruitment_system.screening_service.service;

import com.recruitment_system.event.PostDeadlineEvent;
import com.recruitment_system.screening_service.dto.CandidateResultDto;
import com.recruitment_system.screening_service.feign.JobPostInterface;
import com.recruitment_system.screening_service.feign.ResumeInterface;
import com.recruitment_system.screening_service.model.CandidateResult;
import com.recruitment_system.screening_service.repository.CandidateResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {
    private final ResumeInterface resumeInterface;
    private final JobPostInterface jobPostInterface;
    private final AiScreeningClient aiScreeningClient;
    private final CandidateResultRepository resultRepository;



    @KafkaListener(topics = "post-deadline-event", groupId = "screening-service-group")
    public void screenJobPost(PostDeadlineEvent event) {
        try{
            Long jobPostId = event.getJobPostId();
            var jobPost = jobPostInterface.getJobPostById(jobPostId).getBody().getData();
            var resumes = resumeInterface.getAllByPostId(jobPostId).getBody().getData();
            List<CandidateResult> results = new ArrayList<>();

            List<CandidateResultDto> aiResults = aiScreeningClient.screenedResumes(jobPost, resumes);

            for(CandidateResultDto aiResult : aiResults){
                CandidateResult result = CandidateResult.builder()
                        .resumeId(aiResult.getResumeId())
                        .candidate_name(aiResult.getCandidate_name())
                        .email(aiResult.getEmail())
                        .score(aiResult.getScore())
                        .jobPostId(jobPostId)
                        .match_analysis(aiResult.getMatch_analysis())
                        .build();
                resultRepository.save(result);
            }

        }catch (Exception ex){
            System.out.println("Error processing screening for job post ID " + event.getJobPostId() + ": " + ex.getMessage());

        }

    }

    public List<CandidateResultDto> runScreeningManually(Long postId) {
        var jobPost = jobPostInterface.getJobPostById(postId).getBody().getData();
        var resumes = resumeInterface.getAllByPostId(postId).getBody().getData();
  try{
      List<CandidateResultDto> aiResults = aiScreeningClient.screenedResumes(jobPost, resumes);
      for(CandidateResultDto aiResult : aiResults){
          CandidateResult result = CandidateResult.builder()
                  .resumeId(aiResult.getResumeId())
                  .candidate_name(aiResult.getCandidate_name())
                  .email(aiResult.getEmail())
                  .score(aiResult.getScore())
                  .jobPostId(postId)
                  .match_analysis(aiResult.getMatch_analysis())
                  .build();
          resultRepository.save(result);
      }
      return aiResults;

  }catch (Exception ex){
        System.out.println("Error processing screening for job post ID " + postId + ": " + ex.getMessage());
        return new ArrayList<>();

  }



    }

    public List<CandidateResult> getRankedResults(Long jobPostId) {
        return resultRepository.findByJobPostIdOrderByRankAsc(jobPostId);
    }
}

