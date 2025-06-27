package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.ApiClientDto;
import com.itt.newsAggregation.dto.ApiClientResponseDto;

import java.util.List;

public interface ApiClientService {

    ApiClientResponseDto registerApiClient(ApiClientDto apiClientDto);
    ApiClientResponseDto getApiClientById(Integer id);
    ApiClientResponseDto updateApiClient(Integer id, ApiClientDto apiClientDto);
    String getApiClientByName(String name);
    List<ApiClientResponseDto> getAllClients();
}
