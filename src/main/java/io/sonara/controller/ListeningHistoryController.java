package io.sonara.controller;

import io.sonara.dto.ListeningHistoryResponseDTO;
import io.sonara.service.ListeningHistoryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/history")
public class ListeningHistoryController {

    private final ListeningHistoryService listeningHistoryService;

    public ListeningHistoryController(
            ListeningHistoryService listeningHistoryService
    ) {
        this.listeningHistoryService = listeningHistoryService;
    }

    @PostMapping("/{trackId}")
    public void registerListening(
            @PathVariable UUID trackId,
            Authentication authentication
    ) {
        listeningHistoryService.registerListening(
                trackId,
                authentication
        );
    }

    @GetMapping
    public List<ListeningHistoryResponseDTO> getHistory(
            Authentication authentication
    ) {
        return listeningHistoryService.getHistory(authentication);
    }
}