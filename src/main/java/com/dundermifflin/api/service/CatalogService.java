package com.dundermifflin.api.service;

import com.dundermifflin.api.domain.Catalog;
import com.dundermifflin.api.domain.PaperProduct;
import com.dundermifflin.api.dto.CatalogDto;
import com.dundermifflin.api.dto.PaperProductDto;
import com.dundermifflin.api.repository.CatalogRepository;
import com.dundermifflin.api.repository.PaperProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final PaperProductRepository productRepository;

    public CatalogService(CatalogRepository catalogRepository, PaperProductRepository productRepository) {
        this.catalogRepository = catalogRepository;
        this.productRepository = productRepository;
    }

    public Page<Catalog> list(Pageable pageable) {
        return catalogRepository.findAll(pageable);
    }

    public CatalogDto getById(String catalogId) {
        Catalog c = catalogRepository.findById(catalogId)
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found: " + catalogId));
        return toDto(c);
    }

    public Page<PaperProductDto> listProducts(String catalogId, Pageable pageable) {
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found: " + catalogId));
        List<PaperProductDto> all = new ArrayList<>();
        for (PaperProduct p : catalog.getProducts()) {
            all.add(toProductDto(p));
        }
        int start = Math.toIntExact((long) pageable.getPageNumber() * pageable.getPageSize());
        int end = Math.min(start + pageable.getPageSize(), all.size());
        List<PaperProductDto> content = start >= all.size() ? List.of() : all.subList(start, end);
        return new PageImpl<>(content, pageable, all.size());
    }

    public PaperProductDto addProduct(String catalogId, PaperProductDto newProduct) {
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found: " + catalogId));

        boolean existsInCatalog = catalog.getProducts().stream()
                .anyMatch(p -> p.getName() != null && p.getName().equalsIgnoreCase(newProduct.getName()));
        if (existsInCatalog) {
            throw new IllegalArgumentException("Product name already exists in catalog");
        }

        PaperProduct entity = new PaperProduct();
        entity.setId(UUID.randomUUID().toString());
        entity.setName(newProduct.getName());
        entity.setDescription(newProduct.getDescription());
        entity.setPricePerBox(newProduct.getPricePerBox());
        entity.setPaperWeight(newProduct.getPaperWeight());
        entity.setColor(newProduct.getColor());

        PaperProduct saved = productRepository.save(entity);
        catalog.getProducts().add(saved);
        catalogRepository.save(catalog);
        return toProductDto(saved);
    }

    public void removeProduct(String catalogId, String productId) {
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found: " + catalogId));
        PaperProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
        boolean removed = catalog.getProducts().removeIf(p -> p.getId().equals(product.getId()));
        if (!removed) {
            throw new EntityNotFoundException("Product not associated with catalog");
        }
        catalogRepository.save(catalog);
    }

    private CatalogDto toDto(Catalog c) {
        CatalogDto dto = new CatalogDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());
        return dto;
    }

    private PaperProductDto toProductDto(PaperProduct p) {
        PaperProductDto dto = new PaperProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPricePerBox(p.getPricePerBox());
        dto.setPaperWeight(p.getPaperWeight());
        dto.setColor(p.getColor());
        return dto;
    }
}
