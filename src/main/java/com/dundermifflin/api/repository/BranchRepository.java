package com.dundermifflin.api.repository;

import com.dundermifflin.api.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, String> {
}
