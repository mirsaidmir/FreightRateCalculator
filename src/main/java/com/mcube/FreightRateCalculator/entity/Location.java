package com.mcube.FreightRateCalculator.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String normalizedName;

    private String coordinates;

    private String description;

    public Location (String normalizedName,  String coordinates, String description) {
        this.normalizedName = normalizedName;
        this.coordinates = coordinates;
        this.description = description;
    }
}
