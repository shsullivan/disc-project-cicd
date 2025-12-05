package com.sullivan.disc.dto;



import com.sullivan.disc.model.Manufacturer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>MoldDTO</h1>
 * @author Shawn Sullivan
 * CEN 3024C - 31774
 * July 18, 2025
 * The moldDTO is nested within the discCreateDTO and the DiscDTO classes and converted into Mold objects by the
 * DiscService layer and back to DTOs by the DiscService layer to be sent out to the UI
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoldDTO {

    // Attributes
    private Integer moldId;
    private Integer manufacturer;
    private String mold;
}
