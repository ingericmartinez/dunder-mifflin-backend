package com.dundermifflin.api.web;

import com.dundermifflin.api.dto.CustomerDto;
import com.dundermifflin.api.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/dundermifflin/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Page<CustomerDto> getCustomers(Pageable pageable) {
        return customerService.list(cap(pageable));
    }

    @GetMapping("/{customerId}")
    public CustomerDto getCustomer(@PathVariable String customerId) {
        return customerService.getById(customerId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto dto) {
        CustomerDto created = customerService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/dundermifflin/customers/" + created.getCustomerId()))
                .body(created);
    }

    private Pageable cap(Pageable pageable) {
        int size = Math.min(pageable.getPageSize(), 100);
        return PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
    }
}
