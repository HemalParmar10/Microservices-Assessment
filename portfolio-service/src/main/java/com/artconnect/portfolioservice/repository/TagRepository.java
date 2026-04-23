package com.artconnect.portfolioservice.repository;

import com.artconnect.portfolioservice.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
