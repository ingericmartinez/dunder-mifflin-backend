package com.dundermifflin.api.service;

import com.dundermifflin.api.domain.Branch;
import com.dundermifflin.api.dto.BranchDto;
import com.dundermifflin.api.repository.BranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BranchService {

    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public List<BranchDto> listAll() {
        return branchRepository.findAll().stream().map(this::toDto).toList();
    }

    private BranchDto toDto(Branch b) {
        BranchDto dto = new BranchDto();
        dto.setBranchId(b.getId());
        dto.setName(b.getName());
        dto.setManager(b.getManager());
        return dto;
    }
}
