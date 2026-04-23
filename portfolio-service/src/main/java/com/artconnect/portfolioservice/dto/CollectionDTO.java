package com.artconnect.portfolioservice.dto;

import lombok.Data;

@Data
public class CollectionDTO {
	private Long id;
	private String name;
	private String description;
	private Long artistId;
}
