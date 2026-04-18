package com.dundermifflin.api.service;

import com.dundermifflin.api.domain.Customer;
import com.dundermifflin.api.domain.OrderItem;
import com.dundermifflin.api.domain.PaperProduct;
import com.dundermifflin.api.domain.PurchaseOrder;
import com.dundermifflin.api.dto.OrderDto;
import com.dundermifflin.api.dto.OrderItemDto;
import com.dundermifflin.api.repository.CustomerRepository;
import com.dundermifflin.api.repository.PaperProductRepository;
import com.dundermifflin.api.repository.PurchaseOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final PurchaseOrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final PaperProductRepository productRepository;

    public OrderService(PurchaseOrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        PaperProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public OrderDto create(OrderDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + dto.getCustomerId()));

        PurchaseOrder order = new PurchaseOrder();
        order.setId(UUID.randomUUID().toString());
        order.setCustomer(customer);
        order.setShippingAddress(dto.getShippingAddress());
        order.setStatus("created");
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDto itemDto : dto.getItems()) {
            PaperProduct product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + itemDto.getProductId()));

            BigDecimal pricePerBox = product.getPricePerBox();
            BigDecimal subtotal = pricePerBox.multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            total = total.add(subtotal);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPricePerBox(pricePerBox);
            item.setSubtotal(subtotal);
            items.add(item);
        }

        order.setItems(items);
        order.setTotalAmount(total);

        return toDto(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> list(Pageable pageable) {
        return orderRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public OrderDto getById(String orderId) {
        return orderRepository.findById(orderId)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
    }

    public OrderDto updateStatus(String orderId, String newStatus) {
        PurchaseOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
        order.setStatus(newStatus);
        return toDto(orderRepository.save(order));
    }

    private OrderDto toDto(PurchaseOrder order) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());

        List<OrderItemDto> itemDtos = order.getItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPricePerBox(item.getPricePerBox());
            itemDto.setSubtotal(item.getSubtotal());
            return itemDto;
        }).toList();

        dto.setItems(itemDtos);
        return dto;
    }
}
