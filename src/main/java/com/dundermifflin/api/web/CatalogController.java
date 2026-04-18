package com.dundermifflin.api.web;

import com.dundermifflin.api.dto.CatalogDto;
import com.dundermifflin.api.dto.PaperProductDto;
import com.dundermifflin.api.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping({"/api/v1/dundermifflin/catalogs", "/api/v1/dundermifflin/catalogos"})
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public Page<CatalogDto> getCatalogs(Pageable pageable) {
        Pageable capped = cap(pageable);
        return catalogService.list(capped).map(c -> {
            CatalogDto dto = new CatalogDto();
            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setDescription(c.getDescription());
            return dto;
        });
    }

    @GetMapping("/{catalogId}")
    public CatalogDto getCatalog(@PathVariable String catalogId) {
        return catalogService.getById(catalogId);
    }

    @GetMapping("/{catalogId}/products")
    public Page<PaperProductDto> listProducts(@PathVariable String catalogId, Pageable pageable) {
        Pageable capped = cap(pageable);
        return catalogService.listProducts(catalogId, capped);
    }

    private Pageable cap(Pageable pageable) {
        int size = Math.min(pageable.getPageSize(), 100);
        return PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
    }

    @PostMapping("/{catalogId}/products")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaperProductDto> addProduct(@PathVariable String catalogId,
                                                      @Valid @RequestBody PaperProductDto newProduct) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))) {
            throw new AccessDeniedException("Forbidden");
        }
        PaperProductDto created = catalogService.addProduct(catalogId, newProduct);
        return ResponseEntity.created(URI.create("/api/v1/catalogs/" + catalogId + "/products/" + created.getId()))
                .body(created);
    }

    @DeleteMapping("/{catalogId}/products/{productId}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeProduct(@PathVariable String catalogId, @PathVariable String productId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))) {
            throw new AccessDeniedException("Forbidden");
        }
        catalogService.removeProduct(catalogId, productId);
        return ResponseEntity.noContent().build();
    }
}
