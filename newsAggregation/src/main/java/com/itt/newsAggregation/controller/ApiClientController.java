package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.ApiClientDto;
import com.itt.newsAggregation.service.ApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ApiClientController {

    @Autowired
    private ApiClientService apiClientService;


    @PostMapping
    public ResponseEntity<ApiClientDto> registerClient(@RequestBody ApiClientDto dto) {
        ApiClientDto saved = apiClientService.registerApiClient(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiClientDto> getById(@PathVariable Integer id) {
        ApiClientDto dto = apiClientService.getApiClientById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiClientDto> updateClient(@PathVariable Integer id, @RequestBody ApiClientDto dto) {
        ApiClientDto updated = apiClientService.updateApiClient(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/key")
    public ResponseEntity<String> getApiKeyByName(@RequestParam String name) {
        String apiKey = apiClientService.getApiClientByName(name);
        return ResponseEntity.ok(apiKey);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ApiClientDto>> getAllClients() {
        List<ApiClientDto> allClients = apiClientService.getAllClients();
        if (allClients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allClients, HttpStatus.OK);
    }
}