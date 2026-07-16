package com.example.pdftojson;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ParserService {

    @Value("${ollama.api.url}")
    private String apiUrl;

    @Value("${ollama.model}")
    private String modelName;

    private final RestTemplate restTemplate = new RestTemplate();

    public String parseMarkdownToJson(String markdown) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", modelName);
        body.put("prompt", "Convert the following markdown document into a structured JSON representation. Extract all key information including headers, tables, and text content. Return ONLY raw JSON.\n\nMarkdown:\n" + markdown);
        body.put("format", "json");
        body.put("stream", false);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl + "/api/generate", request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("response");
            }

            throw new RuntimeException("Failed to call Ollama API: " + response.getStatusCode());
        } catch (Exception e) {
            log.error("Error calling Ollama API at {}: {}", apiUrl, e.getMessage());
            throw e;
        }
    }
}
