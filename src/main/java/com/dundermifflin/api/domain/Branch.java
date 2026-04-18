package com.dundermifflin.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {
    @Id
    @Column(name = "branch_id")
    private String id;

    private String name;
    private String manager;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;
}
