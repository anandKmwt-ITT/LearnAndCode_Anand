package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.common.ApiClientDto;
import com.itt.newsAggregation.dto.response.ApiClientResponseDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.ApiClient;
import com.itt.newsAggregation.repository.ApiClientRepository;
import com.itt.newsAggregation.service.impl.ApiClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiClientServiceImplTest {

    @InjectMocks
    private ApiClientServiceImpl apiClientService;

    @Mock
    private ApiClientRepository apiClientRepository;

    @Captor
    private ArgumentCaptor<ApiClient> apiClientCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ApiClientDto getSampleDto() {
        return ApiClientDto.builder()
                .name("TestClient")
                .url("https://api.test.com/")
                .apiKey("12345")
                .status("ACTIVE")
                .build();
    }

    private ApiClient getSampleEntity() {
        return ApiClient.builder()
                .id(1)
                .name("TestClient")
                .url("https://api.test.com/")
                .apiKey("12345")
                .status(ApiClient.Status.ACTIVE)
                .lastAccessed(LocalDateTime.now())
                .build();
    }

    @Test
    void testRegisterApiClient() {
        ApiClientDto dto = getSampleDto();
        ApiClient entity = getSampleEntity();

        when(apiClientRepository.save(any())).thenReturn(entity);

        ApiClientResponseDto response = apiClientService.registerApiClient(dto);

        assertNotNull(response);
        assertEquals(dto.getName(), response.getName());
        verify(apiClientRepository, times(1)).save(any());
    }

    @Test
    void testGetApiClientById_WhenExists() {
        ApiClient entity = getSampleEntity();

        when(apiClientRepository.findById(1)).thenReturn(Optional.of(entity));
        when(apiClientRepository.save(any())).thenReturn(entity);

        ApiClientResponseDto response = apiClientService.getApiClientById(1);

        assertEquals("TestClient", response.getName());
        verify(apiClientRepository).save(apiClientCaptor.capture());
        assertNotNull(apiClientCaptor.getValue().getLastAccessed());
    }

    @Test
    void testGetApiClientById_WhenNotExists() {
        when(apiClientRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> apiClientService.getApiClientById(1));
    }

    @Test
    void testUpdateApiClient_WhenExists() {
        ApiClientDto dto = getSampleDto();
        ApiClient existing = getSampleEntity();

        when(apiClientRepository.findById(1)).thenReturn(Optional.of(existing));
        when(apiClientRepository.save(any())).thenReturn(existing);

        ApiClientResponseDto updated = apiClientService.updateApiClient(1, dto);

        assertEquals(dto.getApiKey(), updated.getApiKey());
        verify(apiClientRepository).save(any());
    }

    @Test
    void testUpdateApiClient_WhenNotExists() {
        when(apiClientRepository.findById(1)).thenReturn(Optional.empty());
        ApiClientDto dto = getSampleDto();

        assertThrows(ResourceNotFoundException.class, () -> apiClientService.updateApiClient(1, dto));
    }

    @Test
    void testGetApiClientByName_WhenExists() {
        ApiClient entity = getSampleEntity();
        when(apiClientRepository.findByNameIgnoreCase("TestClient")).thenReturn(Optional.of(entity));
        when(apiClientRepository.save(any())).thenReturn(entity);

        String fullUrl = apiClientService.getApiClientByName("TestClient");
        assertEquals("https://api.test.com/12345", fullUrl);
    }

    @Test
    void testGetApiClientByName_WhenNotExists() {
        when(apiClientRepository.findByNameIgnoreCase("Unknown")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> apiClientService.getApiClientByName("Unknown"));
    }

    @Test
    void testGetAllClients() {
        List<ApiClient> clients = List.of(getSampleEntity());
        when(apiClientRepository.findAll()).thenReturn(clients);

        List<ApiClientResponseDto> result = apiClientService.getAllClients();

        assertEquals(1, result.size());
        assertEquals("TestClient", result.get(0).getName());
    }
}