package io.sonara.controller;

import io.sonara.service.JamendoImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/import")
public class ImportController {

    private final JamendoImportService jamendoImportService;

    public ImportController(JamendoImportService jamendoImportService) {
        this.jamendoImportService = jamendoImportService;
    }

    @PostMapping("/jamendo")
    public ResponseEntity<Map<String, Integer>> importFromJamendo(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "popularity_month") String order,
            @RequestParam(required = false) String tags
    ) {
        int safeLimit = Math.max(1, Math.min(limit, 100));
        int safeOffset = Math.max(0, offset);

        return ResponseEntity.ok(
                jamendoImportService.importTracks(safeLimit, safeOffset, order, tags)
        );
    }
}