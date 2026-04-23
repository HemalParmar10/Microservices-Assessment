package com.artconnect.portfolioservice.client;

import com.artconnect.portfolioservice.dto.LicenseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "license-service")
public interface LicenseClient {

    @GetMapping("/api/v1/licenses/artist/{artistId}")
    List<LicenseDTO> getLicensesByArtist(@PathVariable Long artistId);

    @GetMapping("/api/v1/licenses/{id}")
    LicenseDTO getLicenseById(@PathVariable Long id);
}
