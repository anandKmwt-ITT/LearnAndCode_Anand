package com.itt.newsAggregation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportedArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    private LocalDateTime reportedAt = LocalDateTime.now();
}
