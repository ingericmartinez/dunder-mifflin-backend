package com.dundermifflin.api.service;

import com.dundermifflin.api.domain.Branch;
import com.dundermifflin.api.domain.Inventory;
import com.dundermifflin.api.domain.PaperProduct;
import com.dundermifflin.api.repository.BranchRepository;
import com.dundermifflin.api.repository.InventoryRepository;
import com.dundermifflin.api.repository.PaperProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InventoryServiceTest {

    private InventoryRepository inventoryRepository;
    private BranchRepository branchRepository;
    private PaperProductRepository productRepository;
    private InventoryService service;

    @BeforeEach
    void setUp() {
        inventoryRepository = Mockito.mock(InventoryRepository.class);
        branchRepository = Mockito.mock(BranchRepository.class);
        productRepository = Mockito.mock(PaperProductRepository.class);
        service = new InventoryService(inventoryRepository, branchRepository, productRepository);
    }

    @Test
    void updateQuantity_throwsOnNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateQuantity("p1", "b1", -1));
        assertTrue(ex.getMessage().contains("quantityInStock"));
    }

    @Test
    void updateQuantity_happyPath() {
        PaperProduct product = PaperProduct.builder().id("p1").name("p").build();
        Branch branch = Branch.builder().id("b1").name("b").build();
        Inventory inv = Inventory.builder().id(1).product(product).branch(branch).quantityInStock(5).build();
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));
        when(branchRepository.findById("b1")).thenReturn(Optional.of(branch));
        when(inventoryRepository.findByProductAndBranch(product, branch)).thenReturn(Optional.of(inv));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(a -> a.getArgument(0, Inventory.class));

        var dto = service.updateQuantity("p1", "b1", 10);
        assertEquals(10, dto.getQuantityInStock());
        assertEquals("p1", dto.getProductId());
        assertEquals("b1", dto.getBranchId());
    }

    @Test
    void updateQuantity_notFoundPaths() {
        when(productRepository.findById("p1")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.updateQuantity("p1", "b1", 1));
    }
}
