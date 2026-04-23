package com.artconnect.portfolioservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

	String storeFile(MultipartFile file);
}
