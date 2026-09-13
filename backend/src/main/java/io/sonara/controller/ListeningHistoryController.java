package io.sonara.controller;

import io.sonara.dto.ListeningHistoryResponseDTO;
import io.sonara.entity.User;
import io.sonara.service.ListeningHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/history")
public class ListeningHistoryController {

    private final ListeningHistoryService listeningHistoryService;

    public ListeningHistoryController(ListeningHistoryService listeningHistoryService) {
        this.listeningHistoryService = listeningHistoryService;
    }

    @PostMapping("/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerListening(
            @PathVariable UUID trackId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        listeningHistoryService.registerListening(trackId, user);
    }

    @GetMapping
    public List<ListeningHistoryResponseDTO> getHistory(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return listeningHistoryService.getHistory(user);
    }
}