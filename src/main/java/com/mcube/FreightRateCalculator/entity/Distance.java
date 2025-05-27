package com.mcube.FreightRateCalculator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Distance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "origin_id")
    Location origin;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    Location destination;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal distance;

    @Column
    LocalDate fetchDate;
}
