package com.artconnect.portfolioservice.controller;

import com.artconnect.portfolioservice.entity.Collection;
import com.artconnect.portfolioservice.repository.CollectionRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionRepository collectionRepository;

    @PostMapping
    public Collection create(@RequestBody Collection collection) {
        return collectionRepository.save(collection);
    }

    @GetMapping
    public List<Collection> getAll() {
        return collectionRepository.findAll();
    }
}
