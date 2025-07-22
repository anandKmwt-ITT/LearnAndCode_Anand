package com.itt.newsAggregation.repository;

import com.itt.newsAggregation.model.ApiClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiClientRepository extends JpaRepository<ApiClient, Integer> {
    Optional<ApiClient> findByNameIgnoreCase(String name);
}
