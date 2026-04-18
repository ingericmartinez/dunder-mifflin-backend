package com.dundermifflin.api.service;

import com.dundermifflin.api.domain.Customer;
import com.dundermifflin.api.dto.CustomerDto;
import com.dundermifflin.api.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Page<CustomerDto> list(Pageable pageable) {
        return customerRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public CustomerDto getById(String id) {
        return customerRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
    }

    public CustomerDto create(CustomerDto dto) {
        customerRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
            throw new DataIntegrityViolationException("A customer with email '" + dto.getEmail() + "' already exists");
        });

        Customer entity = new Customer();
        entity.setId(UUID.randomUUID().toString());
        entity.setName(dto.getName());
        entity.setContactPerson(dto.getContactPerson());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return toDto(customerRepository.save(entity));
    }

    private CustomerDto toDto(Customer c) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(c.getId());
        dto.setName(c.getName());
        dto.setContactPerson(c.getContactPerson());
        dto.setEmail(c.getEmail());
        dto.setPhone(c.getPhone());
        dto.setAddress(c.getAddress());
        return dto;
    }
}
