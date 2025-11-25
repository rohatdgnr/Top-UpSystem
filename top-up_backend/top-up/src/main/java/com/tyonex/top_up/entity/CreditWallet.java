package com.tyonex.top_up.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_wallets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long companyId;

    @Column(nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal creditBalance = BigDecimal.ZERO;

    @Version
    private Long version; // Optimistic locking

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;
}

