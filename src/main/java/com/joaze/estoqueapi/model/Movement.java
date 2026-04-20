package com.joaze.estoqueapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movement")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementStatus status;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitCost;

    @Column(nullable = false)
    private BigDecimal totalValue;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime correctedAt;

    private LocalDateTime canceledAt;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "movement_reference_id")
    Movement movementReference;

    @PrePersist
    private void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}