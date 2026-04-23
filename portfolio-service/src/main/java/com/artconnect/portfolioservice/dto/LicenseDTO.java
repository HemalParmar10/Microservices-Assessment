package com.artconnect.portfolioservice.dto;

import lombok.Data;

@Data
public class LicenseDTO {

    private Long id;
    private Long artworkId;
    private Long artistId;
    private String licenseType;
    private Double price;
}
