package com.dundermifflin.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paper_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaperProduct {
    @Id
    @Column(name = "product_id")
    private String id;

    private String name;
    private String description;

    @Column(name = "price_per_box")
    private java.math.BigDecimal pricePerBox;

    @Column(name = "paper_weight")
    private String paperWeight;

    private String color;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;
}
