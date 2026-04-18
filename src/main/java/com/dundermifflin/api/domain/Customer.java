package com.dundermifflin.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @Column(name = "customer_id")
    private String id;

    private String name;

    @Column(name = "contact_person")
    private String contactPerson;

    private String email;
    private String phone;
    private String address;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;
}
