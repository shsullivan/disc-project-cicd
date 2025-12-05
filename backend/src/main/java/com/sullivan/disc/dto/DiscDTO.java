package com.sullivan.disc.dto;

import com.sullivan.disc.model.Disc;
import lombok.*;

import java.math.BigDecimal;

/**
 * <h1>DiscDTO</h1>
 * @author Shawn Sullivan
 * CEN 3024C - 31774
 * July 18, 2025
 * This class is the main DTO utilized by the several methods in the DiscService class to provide disc
 * information to the frontend to be displayed to the user and disc information to the backend to interact with records
 */
// Lombok annotations to avoid boiler plate code
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DiscDTO {

    // Attributes
    private Integer discID;
    private ManufacturerDTO manufacturer;
    private MoldDTO mold;
    private String plastic;
    private String color;
    private Integer condition;
    private String description;
    private ContactDTO contact;
    private String foundAt; // Course where disc was found
    private boolean returned;
    private boolean sold;
    private BigDecimal MSRP;
    private BigDecimal resaleValue;
}
