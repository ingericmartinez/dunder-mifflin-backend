package com.dundermifflin.api.repository;

import com.dundermifflin.api.domain.PaperProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaperProductRepository extends JpaRepository<PaperProduct, String> {
    Optional<PaperProduct> findByNameIgnoreCase(String name);
}
