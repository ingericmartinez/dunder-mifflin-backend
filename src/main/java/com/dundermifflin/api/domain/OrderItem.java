package com.dundermifflin.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private PurchaseOrder order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private PaperProduct product;

    private Integer quantity;

    @Column(name = "price_per_box")
    private BigDecimal pricePerBox;

    private BigDecimal subtotal;
}
