package com.dundermifflin.api.web;

import com.dundermifflin.api.dto.OrderDto;
import com.dundermifflin.api.dto.UpdateOrderStatusRequest;
import com.dundermifflin.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/dundermifflin/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Page<OrderDto> listOrders(Pageable pageable) {
        int size = Math.min(pageable.getPageSize(), 100);
        return orderService.list(PageRequest.of(pageable.getPageNumber(), size, pageable.getSort()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto dto) {
        OrderDto created = orderService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/dundermifflin/orders/" + created.getOrderId()))
                .body(created);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable String orderId) {
        return orderService.getById(orderId);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto updateOrderStatus(@PathVariable String orderId,
                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateStatus(orderId, request.getStatus());
    }
}
