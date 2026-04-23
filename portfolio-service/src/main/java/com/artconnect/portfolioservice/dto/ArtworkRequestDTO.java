package com.artconnect.portfolioservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ArtworkRequestDTO {

    private String title;
    private String description;
    private String imageUrl;
    private String resolution;

    private Set<Long> tagIds;
    private Set<Long> collectionIds;
}
