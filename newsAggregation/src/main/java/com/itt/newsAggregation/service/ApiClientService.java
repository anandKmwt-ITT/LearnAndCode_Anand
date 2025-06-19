package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.ApiClientDto;

import java.util.List;

public interface ApiClientService {

    ApiClientDto registerApiClient(ApiClientDto apiClientDto);
    ApiClientDto getApiClientById(Integer id);
    ApiClientDto updateApiClient(Integer id, ApiClientDto apiClientDto);
    String getApiClientByName(String name);
    List<ApiClientDto> getAllClients();
}
