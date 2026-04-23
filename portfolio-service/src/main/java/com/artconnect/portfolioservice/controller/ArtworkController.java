package com.artconnect.portfolioservice.controller;

import com.artconnect.portfolioservice.dto.ArtworkRequestDTO;
import com.artconnect.portfolioservice.dto.ArtworkResponseDTO;
import com.artconnect.portfolioservice.service.ArtworkService;
import com.artconnect.portfolioservice.service.FileStorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/artworks")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkService artworkService;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<ArtworkResponseDTO> create(
            @RequestBody ArtworkRequestDTO dto,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(artworkService.createArtwork(dto, email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtworkResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(artworkService.getArtworkById(id));
    }

    @GetMapping
    public ResponseEntity<List<ArtworkResponseDTO>> getAll() {
        return ResponseEntity.ok(artworkService.getAllArtworks());
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<ArtworkResponseDTO>> getByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(artworkService.getByArtist(artistId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        artworkService.deleteArtwork(id);
        return ResponseEntity.ok("Artwork deleted successfully");
    }

    private Set<Long> parseIds(String ids) {
        if (ids == null || ids.isBlank()) {
            return Collections.emptySet();
        }

        return Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    @PostMapping("/upload")
    public ResponseEntity<ArtworkResponseDTO> createWithImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("resolution") String resolution,
            @RequestParam("tagIds") String tagIds,
            @RequestParam("collectionIds") String collectionIds,
            Authentication authentication
    ) {

        String imageUrl = fileStorageService.storeFile(file);
        String email = authentication.getName();

        ArtworkRequestDTO dto = new ArtworkRequestDTO();
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setResolution(resolution);
        dto.setImageUrl(imageUrl);
        dto.setTagIds(parseIds(tagIds));
        dto.setCollectionIds(parseIds(collectionIds));

        return ResponseEntity.ok(artworkService.createArtwork(dto, email));
    }
}
