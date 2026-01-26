package com.example.java_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Service
public class ExchangeService {
    private final RestTemplate restTemplate;

    public ExchangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getJsonPython(Map<String, Object> json) throws IOException, InterruptedException {
        try {
            System.out.println(json);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(json, headers);

            // Отправляю POST
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "http://127.0.0.1:8080/predictPy",
                    HttpMethod.POST,
                    entity,  // Передаю entity с телом запроса
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Ошибка при получении данных: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить данные из Python сервиса", e);
        }
    }
}
