package com.artconnect.portfolioservice.service.impl;

import com.artconnect.portfolioservice.client.ArtistClient;
import com.artconnect.portfolioservice.client.NotificationClient;
import com.artconnect.portfolioservice.dto.ArtistDTO;
import com.artconnect.portfolioservice.dto.ArtworkRequestDTO;
import com.artconnect.portfolioservice.dto.ArtworkResponseDTO;
import com.artconnect.portfolioservice.dto.NotificationRequest;
import com.artconnect.portfolioservice.entity.Artwork;
import com.artconnect.portfolioservice.entity.Collection;
import com.artconnect.portfolioservice.entity.Tag;
import com.artconnect.portfolioservice.repository.ArtworkRepository;
import com.artconnect.portfolioservice.repository.CollectionRepository;
import com.artconnect.portfolioservice.repository.TagRepository;
import com.artconnect.portfolioservice.service.ArtworkService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final TagRepository tagRepository;
    private final CollectionRepository collectionRepository;
    private final ArtistClient artistClient;
    private final NotificationClient notificationClient;

    @Override
    public ArtworkResponseDTO createArtwork(ArtworkRequestDTO dto, String email) {

        // 1️ Fetch artist from artist-service via Feign
        ArtistDTO artist = artistClient.getArtistByEmail(email);

        // 2️ Validate tags
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(dto.getTagIds()));
        if (tags.size() != dto.getTagIds().size()) {
            throw new RuntimeException("Some tag IDs are invalid");
        }

        // 3️ Validate collections
        Set<Collection> collections =
                new HashSet<>(collectionRepository.findAllById(dto.getCollectionIds()));
        if (collections.size() != dto.getCollectionIds().size()) {
            throw new RuntimeException("Some collection IDs are invalid");
        }

        // 4️ Build and save artwork
        Artwork artwork = Artwork.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .artistId(artist.getId())
                .imageUrl(dto.getImageUrl())
                .resolution(dto.getResolution())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .tags(tags)
                .collections(collections)
                .build();

        Artwork saved = artworkRepository.save(artwork);

        // 5️ Trigger email notification via notification-service (non-blocking best-effort)
        try {
            NotificationRequest notificationRequest =
                    new NotificationRequest(saved.getId(), email);

            notificationClient.notifyArtworkUploaded(notificationRequest);
            log.info("✅ Notification triggered for artworkId={} artistEmail={}", saved.getId(), email);

        } catch (Exception e) {
            // Notification failure must NOT fail the artwork creation
            log.warn("⚠️ Notification call failed for artworkId={} | Reason: {}",
                    saved.getId(), e.getMessage());
        }

        return mapToDTO(saved);
    }

    @Override
    public ArtworkResponseDTO getArtworkById(Long id) {
        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artwork not found"));
        return mapToDTO(artwork);
    }

    @Override
    public List<ArtworkResponseDTO> getAllArtworks() {
        return artworkRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ArtworkResponseDTO> getByArtist(Long artistId) {
        return artworkRepository.findByArtistId(artistId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
    }

    // ─── Mapper ──────────────────────────────────────────────────────────────

    private ArtworkResponseDTO mapToDTO(Artwork artwork) {

        Set<String> tagNames = artwork.getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        Set<String> collectionNames = artwork.getCollections()
                .stream()
                .map(Collection::getName)
                .collect(Collectors.toSet());

        return ArtworkResponseDTO.builder()
                .id(artwork.getId())
                .title(artwork.getTitle())
                .description(artwork.getDescription())
                .artistId(artwork.getArtistId())
                .imageUrl(artwork.getImageUrl())
                .resolution(artwork.getResolution())
                .tags(tagNames)
                .collections(collectionNames)
                .build();
    }
}
