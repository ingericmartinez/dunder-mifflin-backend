package com.dundermifflin.api.web;

import com.dundermifflin.api.dto.InventoryItemDto;
import com.dundermifflin.api.dto.UpdateInventoryQuantityRequest;
import com.dundermifflin.api.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/v1/dundermifflin/inventory", "/api/v1/dundermifflin/inventario"})
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public Page<InventoryItemDto> list(@RequestParam(required = false) String branchId, Pageable pageable) {
        Pageable capped = cap(pageable);
        return inventoryService.list(branchId, capped);
    }

    @GetMapping("/{productId}")
    public InventoryItemDto getItem(@PathVariable String productId, @RequestParam String branchId) {
        return inventoryService.getItem(productId, branchId);
    }

    @PatchMapping("/{productId}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public InventoryItemDto update(@PathVariable String productId,
                                   @RequestParam String branchId,
                                   @Valid @RequestBody UpdateInventoryQuantityRequest payload) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))) {
            throw new AccessDeniedException("Forbidden");
        }
        return inventoryService.updateQuantity(productId, branchId, payload.getQuantityInStock());
    }

    private Pageable cap(Pageable pageable) {
        int size = Math.min(pageable.getPageSize(), 100);
        return PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
    }
}
