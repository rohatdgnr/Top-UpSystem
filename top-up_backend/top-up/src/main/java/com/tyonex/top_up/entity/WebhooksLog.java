package com.tyonex.top_up.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhooks_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhooksLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    @Builder.Default
    private Boolean processed = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

