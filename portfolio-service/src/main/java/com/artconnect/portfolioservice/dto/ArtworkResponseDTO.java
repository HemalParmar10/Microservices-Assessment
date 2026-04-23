package com.artconnect.portfolioservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ArtworkResponseDTO {

	private Long id;
	private String title;
	private String description;
	private Long artistId;
	private String imageUrl;
	private String resolution;

	private Set<String> tags;
	private Set<String> collections;
}
