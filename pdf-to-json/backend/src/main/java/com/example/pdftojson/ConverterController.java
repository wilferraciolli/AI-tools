package com.example.pdftojson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/converter")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class ConverterController {

    private final OcrService ocrService;
    private final ParserService parserService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAndConvert(@RequestParam("file") MultipartFile file) {
        log.info("Received file upload: {}", file.getOriginalFilename());
        try {
            // 1. Convert PDF to Markdown using GLM-OCR
            log.info("Step 1: Converting PDF to Markdown via Marker API...");
            String markdown = ocrService.convertPdfToMarkdown(file);
            log.info("Successfully converted to Markdown (length: {})", markdown.length());
            
            // 2. Convert Markdown to JSON using GLM-4
            log.info("Step 2: Parsing Markdown to JSON via Ollama...");
            String jsonResult = parserService.parseMarkdownToJson(markdown);
            log.info("Successfully parsed to JSON");
            
            return ResponseEntity.ok(jsonResult);
        } catch (IOException e) {
            log.error("File processing error", e);
            return ResponseEntity.internalServerError().body("File processing error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during conversion", e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
