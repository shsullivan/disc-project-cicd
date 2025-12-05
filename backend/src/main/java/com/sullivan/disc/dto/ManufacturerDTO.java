package com.sullivan.disc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>ManufacturerdDTO</h1>
 * @author Shawn Sullivan
 * CEN 3024C - 31774
 * July 18, 2025
 * The ManufacturerDTO is nested within the discCreateDTO and the DiscDTO classes and converted into manufacturer
 * objects by the DiscService layer and back to DTOs by the DiscService layer to be sent out to the UI
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerDTO {

    // Attributes
    private Integer manufacturerId;
    private String manufacturer;
}
