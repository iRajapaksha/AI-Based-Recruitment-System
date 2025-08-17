package com.recruitment_system.screening_service.service;

import com.recruitment_system.screening_service.feign.ResumeInterface;
import com.recruitment_system.screening_service.model.ScreeningResult;
import com.recruitment_system.screening_service.repository.ScreeningResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {
    private final ResumeInterface resumeInterface;
    private final AiScreeningClient aiScreeningClient;
    private final ScreeningResultRepository resultRepository;

    public void screenJobPost(Long jobPostId, String jobDescription) {
        var response = resumeInterface.getAllByPostId(jobPostId);
        var resumes = response.getBody().getData();
        List<ScreeningResult> results = new ArrayList<>();

        for (var resume : resumes) {
            var aiResult = aiScreeningClient.screenCv(resume.getFileURI(), jobDescription);
            results.add(new ScreeningResult(
                    null,
                    resume.getId(),
                    jobPostId,
                    aiResult.getScore(),
                    aiResult.getRank(),
                    LocalDateTime.now()
            ));
        }

        resultRepository.saveAll(results);
    }

    public List<ScreeningResult> getRankedResults(Long jobPostId) {
        return resultRepository.findByJobPostIdOrderByRankAsc(jobPostId);
    }
}

