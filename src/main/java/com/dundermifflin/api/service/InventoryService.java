package com.dundermifflin.api.service;

import com.dundermifflin.api.domain.Branch;
import com.dundermifflin.api.domain.Inventory;
import com.dundermifflin.api.domain.PaperProduct;
import com.dundermifflin.api.dto.InventoryItemDto;
import com.dundermifflin.api.repository.BranchRepository;
import com.dundermifflin.api.repository.InventoryRepository;
import com.dundermifflin.api.repository.PaperProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final BranchRepository branchRepository;
    private final PaperProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository, BranchRepository branchRepository, PaperProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    public Page<InventoryItemDto> list(String branchId, Pageable pageable) {
        if (branchId == null) {
            return inventoryRepository.findAll(pageable).map(this::toDto);
        }
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new EntityNotFoundException("Branch not found: " + branchId));
        return inventoryRepository.findAllByBranch(branch, pageable).map(this::toDto);
    }

    public InventoryItemDto getItem(String productId, String branchId) {
        PaperProduct product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new EntityNotFoundException("Branch not found: " + branchId));
        Inventory inv = inventoryRepository.findByProductAndBranch(product, branch)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product and branch"));
        return toDto(inv);
    }

    public InventoryItemDto updateQuantity(String productId, String branchId, int newQty) {
        if (newQty < 0) throw new IllegalArgumentException("quantityInStock must be >= 0");
        PaperProduct product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new EntityNotFoundException("Branch not found: " + branchId));
        Inventory inv = inventoryRepository.findByProductAndBranch(product, branch)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product and branch"));
        inv.setQuantityInStock(newQty);
        inv.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        Inventory saved = inventoryRepository.save(inv);
        return toDto(saved);
    }

    private InventoryItemDto toDto(Inventory inv) {
        InventoryItemDto dto = new InventoryItemDto();
        dto.setProductId(inv.getProduct().getId());
        dto.setProductName(inv.getProduct().getName());
        dto.setBranchId(inv.getBranch().getId());
        dto.setBranchName(inv.getBranch().getName());
        dto.setQuantityInStock(inv.getQuantityInStock());
        return dto;
    }
}
