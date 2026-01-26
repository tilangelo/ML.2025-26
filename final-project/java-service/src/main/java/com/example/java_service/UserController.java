package com.example.java_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    private final ExchangeService exchangeService;

    public UserController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/")
    public String getMainPage() {
        return "index";
    }

    @PostMapping("/predict")
    @ResponseBody
    public ResponseEntity<?> predict(String message) throws IOException, InterruptedException {

        Map<String, Object> request = Map.of("text", message);

        Map<String, Object> response = exchangeService.getJsonPython(request);



        return ResponseEntity.ok(response);
    }
}
