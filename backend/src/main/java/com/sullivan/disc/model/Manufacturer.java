package com.sullivan.disc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>Manufacturer Entity</h1>
 * @author Shawn Sullivan
 * CEN 3024C-31774
 * July 12, 2025
 * This model/entity class mirrors the "manufacturers" database table and is required to implement JPA database
 * connectivity.
 */
@Entity
@Table(name = "manufacturers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manufacturer {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id", nullable = false)
    private Integer manufacturerId;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

}
