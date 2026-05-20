package io.sonara.controller;

import io.sonara.dto.AlbumRequestDTO;
import io.sonara.entity.Album;
import io.sonara.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping
    public Album save(@RequestBody @Valid AlbumRequestDTO dto) {
        return albumService.save(dto);
    }

    @GetMapping
    public List<Album> findAll() {
        return albumService.findAll();
    }
}