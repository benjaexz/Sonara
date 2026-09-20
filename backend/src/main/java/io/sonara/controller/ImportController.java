package io.sonara.controller;

import io.sonara.service.JamendoImportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@RestController
@RequestMapping("/admin/import")
public class ImportController {

    private final JamendoImportService jamendoImportService;

    @Value("${ADMIN_IMPORT_KEY:}")
    private String adminKey;

    public ImportController(JamendoImportService jamendoImportService) {
        this.jamendoImportService = jamendoImportService;
    }

    @PostMapping("/jamendo")
    public ResponseEntity<Map<String, Integer>> importFromJamendo(
            @RequestHeader(value = "X-Admin-Key", required = false) String providedKey,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "popularity_month") String order,
            @RequestParam(required = false) String tags
    ) {
        if (!isAuthorized(providedKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        int safeLimit = Math.max(1, Math.min(limit, 100));
        int safeOffset = Math.max(0, offset);

        return ResponseEntity.ok(
                jamendoImportService.importTracks(safeLimit, safeOffset, order, tags)
        );
    }

    private boolean isAuthorized(String providedKey) {
        if (adminKey == null || adminKey.isBlank() || providedKey == null) {
            return false;
        }

        return MessageDigest.isEqual(
                adminKey.getBytes(StandardCharsets.UTF_8),
                providedKey.getBytes(StandardCharsets.UTF_8)
        );
    }
}