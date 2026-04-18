package com.dundermifflin.api.repository;

import com.dundermifflin.api.domain.Inventory;
import com.dundermifflin.api.domain.Branch;
import com.dundermifflin.api.domain.PaperProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Page<Inventory> findAllByBranch(Branch branch, Pageable pageable);
    Optional<Inventory> findByProductAndBranch(PaperProduct product, Branch branch);
}
