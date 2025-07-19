package com.itt.newsAggregation.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    private boolean hidden = false;

    @OneToMany(mappedBy = "category")
    private List<Article> article;

    @OneToMany(mappedBy = "category")
    private List<Keyword> keywords;
}
