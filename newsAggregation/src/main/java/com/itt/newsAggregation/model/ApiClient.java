package com.itt.newsAggregation.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "api_client")
public class ApiClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String apiKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    private LocalDateTime lastAccessed;

    public enum Status {
        ACTIVE,
        NOT_ACTIVE
    }
}