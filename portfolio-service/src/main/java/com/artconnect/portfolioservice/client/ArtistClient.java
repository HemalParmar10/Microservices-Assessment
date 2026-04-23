package com.artconnect.portfolioservice.client;

import com.artconnect.portfolioservice.dto.ArtistDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "artist-service")
public interface ArtistClient {

    @GetMapping("/api/v1/artists/by-email")
    ArtistDTO getArtistByEmail(@RequestParam String email);
}
