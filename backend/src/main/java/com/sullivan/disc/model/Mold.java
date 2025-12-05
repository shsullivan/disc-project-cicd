package com.sullivan.disc.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>Mold Entity</h1>
 * @author Shawn Sullivan
 * CEN 3024C-31774
 * July 12, 2025
 * This model/entity class mirrors the "molds" database table and is required to implement JPA database connectivity.
 */
@Entity
@Table(name = "molds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mold {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mold_id")
    private Integer moldID;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "mold")
    private String mold;
}
