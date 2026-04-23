package com.artconnect.portfolioservice.repository;

import com.artconnect.portfolioservice.entity.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

	List<Artwork> findByArtistId(Long artistId);
}
