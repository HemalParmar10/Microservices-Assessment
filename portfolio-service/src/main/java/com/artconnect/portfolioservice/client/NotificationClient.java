package com.artconnect.portfolioservice.client;

import com.artconnect.portfolioservice.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

	@PostMapping("/api/v1/notifications/artwork")
	void notifyArtworkUploaded(@RequestBody NotificationRequest request);
}
