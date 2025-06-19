package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.ApiClientDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.ApiClient;
import com.itt.newsAggregation.repositoy.ApiClientRepository;
import com.itt.newsAggregation.service.ApiClientService;
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

    @Override
    public ApiClientDto registerApiClient(ApiClientDto dto) {
        ApiClient apiClient = mapToEntity.apply(dto);
        ApiClient saved = apiClientRepository.save(apiClient);
        return mapToDto(saved);
    }

    @Override
    public ApiClientDto getApiClientById(Integer id) {
        ApiClient apiClient = apiClientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API client not found with ID: " + id));

        apiClient.setLastAccessed(LocalDateTime.now());
        apiClient = apiClientRepository.save(apiClient);

        return mapToDto(apiClient);
    }

    @Override
    public ApiClientDto updateApiClient(Integer id, ApiClientDto dto) {
        ApiClient existing = apiClientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API client not found with ID: " + id));

        existing.setName(dto.getName());
        existing.setUrl(dto.getUrl());
        existing.setApiKey(dto.getApiKey());
        existing.setStatus(ApiClient.Status.valueOf(dto.getStatus().toUpperCase()));
        existing.setLastAccessed(LocalDateTime.now());

        ApiClient updated = apiClientRepository.save(existing);
        return mapToDto(updated);
    }

    @Override
    public String getApiClientByName(String name) {
        ApiClient apiClient = apiClientRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("API client not found with name: " + name));

        String fullUrl = apiClient.getUrl()+apiClient.getApiKey();

        apiClient.setLastAccessed(LocalDateTime.now());
        apiClientRepository.save(apiClient);

        return fullUrl;
    }

    @Override
    public List<ApiClientDto> getAllClients() {
        List<ApiClient> apiClients = apiClientRepository.findAll();
        List<ApiClientDto> apiClientDtos = apiClients.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return apiClientDtos;
    }


    private ApiClientDto mapToDto(ApiClient apiClient) {
        return ApiClientDto.builder()
                .name(apiClient.getName())
                .url(apiClient.getUrl())
                .apiKey(apiClient.getApiKey())
                .status(apiClient.getStatus().name())
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
