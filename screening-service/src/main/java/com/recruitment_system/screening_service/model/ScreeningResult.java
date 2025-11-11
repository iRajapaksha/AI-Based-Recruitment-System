package com.recruitment_system.screening_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "screening_results")
@CompoundIndexes({
        @CompoundIndex(name = "job_post_idx", def = "{'jobPostId': 1}"),
        @CompoundIndex(name = "score_desc_idx", def = "{'jobPostId': 1, 'score': -1}")
})
public class ScreeningResult {

    private String id; // Mongo id is string (ObjectId)
    private Long jobPostId;
    private Long applicationId;
    private String candidateName;

    // large text fields
    private String cvSummary;
    private String githubSummary;
    private String email;
    private String matchAnalysis;
    private double score;
}
