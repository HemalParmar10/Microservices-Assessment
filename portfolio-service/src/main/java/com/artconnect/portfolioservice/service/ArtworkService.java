package com.artconnect.portfolioservice.service;

import com.artconnect.portfolioservice.dto.ArtworkRequestDTO;
import com.artconnect.portfolioservice.dto.ArtworkResponseDTO;

import java.util.List;

public interface ArtworkService {

    ArtworkResponseDTO createArtwork(ArtworkRequestDTO dto, String email);

    ArtworkResponseDTO getArtworkById(Long id);

    List<ArtworkResponseDTO> getAllArtworks();

    List<ArtworkResponseDTO> getByArtist(Long artistId);

    void deleteArtwork(Long id);
}
