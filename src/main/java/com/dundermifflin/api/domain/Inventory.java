package com.dundermifflin.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "branch_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private PaperProduct product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;

    @Column(name = "last_updated")
    private java.sql.Timestamp lastUpdated;
}
