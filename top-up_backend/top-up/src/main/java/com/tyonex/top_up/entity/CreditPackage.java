package com.tyonex.top_up.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_packages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer creditAmount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(columnDefinition = "TEXT")
    private String description;
}

