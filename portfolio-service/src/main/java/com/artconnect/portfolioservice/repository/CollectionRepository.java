package com.artconnect.portfolioservice.repository;

import com.artconnect.portfolioservice.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

	List<Collection> findByArtistId(Long artistId);
}
