package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.common.ApiClientDto;
import com.itt.newsAggregation.dto.response.ApiClientResponseDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.ApiClient;
import com.itt.newsAggregation.repository.ApiClientRepository;
import com.itt.newsAggregation.service.ApiClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApiClientServiceImpl implements ApiClientService {

    @Autowired
    private ApiClientRepository apiClientRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApiClientServiceImpl.class);

    @Override
    public ApiClientResponseDto registerApiClient(ApiClientDto dto) {
        logger.info("Registering new API client: {}", dto.getName());
        ApiClient apiClient = mapToEntity.apply(dto);
        ApiClient saved = apiClientRepository.save(apiClient);
        logger.debug("Registered API client with ID: {}", saved.getId());
        return mapToDto(saved);
    }

    @Override
    public ApiClientResponseDto getApiClientById(Integer id) {
        logger.info("Fetching API client by ID: {}", id);
        ApiClient apiClient = apiClientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API client not found with ID: " + id));

        apiClient.setLastAccessed(LocalDateTime.now());
        apiClient = apiClientRepository.save(apiClient);
        logger.debug("Updated last accessed time for client ID: {}", id);
        return mapToDto(apiClient);
    }

    @Override
    public ApiClientResponseDto updateApiClient(Integer id, ApiClientDto dto) {
        logger.info("Updating API client with ID: {}", id);
        ApiClient existing = apiClientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API client not found with ID: " + id));

        existing.setName(dto.getName());
        existing.setUrl(dto.getUrl());
        existing.setApiKey(dto.getApiKey());
        existing.setStatus(ApiClient.Status.valueOf(dto.getStatus().toUpperCase()));
        existing.setLastAccessed(LocalDateTime.now());

        ApiClient updated = apiClientRepository.save(existing);
        logger.debug("Updated API client: {}", updated.getId());
        return mapToDto(updated);
    }

    @Override
    public String getApiClientByName(String name) {
        logger.info("Fetching API client by name: {}", name);
        ApiClient apiClient = apiClientRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("API client not found with name: " + name));

        String fullUrl = apiClient.getUrl()+apiClient.getApiKey();

        apiClient.setLastAccessed(LocalDateTime.now());
        apiClientRepository.save(apiClient);

        return fullUrl;
    }

    @Override
    public List<ApiClientResponseDto> getAllClients() {
        logger.info("Fetching all API clients");
        List<ApiClient> apiClients = apiClientRepository.findAll();
        List<ApiClientResponseDto> apiClientDtos = apiClients.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return apiClientDtos;
    }


    private ApiClientResponseDto mapToDto(ApiClient apiClient) {
        return ApiClientResponseDto.builder()
                .id(apiClient.getId())
                .name(apiClient.getName())
                .url(apiClient.getUrl())
                .apiKey(apiClient.getApiKey())
                .status(apiClient.getStatus().name())
                .lastAccessed(apiClient.getLastAccessed().toString())
                .build();
    }

    private Function<ApiClientDto, ApiClient> mapToEntity = dto -> ApiClient.builder()
            .name(dto.getName())
            .url(dto.getUrl())
            .apiKey(dto.getApiKey())
            .status(ApiClient.Status.valueOf(dto.getStatus().toUpperCase()))
            .lastAccessed(LocalDateTime.now())
            .build();
}
