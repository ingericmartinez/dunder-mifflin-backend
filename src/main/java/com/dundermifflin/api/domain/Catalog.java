package com.dundermifflin.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "catalogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Catalog {
    @Id
    @Column(name = "catalog_id")
    private String id;

    private String name;
    private String description;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    @ManyToMany
    @JoinTable(
            name = "catalog_products",
            joinColumns = @JoinColumn(name = "catalog_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<PaperProduct> products = new HashSet<>();
}
