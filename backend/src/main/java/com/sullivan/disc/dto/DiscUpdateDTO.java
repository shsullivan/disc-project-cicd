package com.sullivan.disc.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <h1>DiscUpdateDTO</h1>
 * @author Shawn Sullivan
 * CEN 3024C - 31774
 * July 18, 2025
 * This class is a specialty DTO utilized by the UpdateDisc() method in the DiscService class to provide the backend
 * information for an existing disc in the DB that needs to have fields updated.
 */
// Lombok annotation to avoid boilerplate
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscUpdateDTO {

    // Attributes with validation annotation
    @NotNull(message = "Manufacturer is required")
    private ManufacturerDTO manufacturer;

    @NotNull(message = "Mold is required")
    private MoldDTO mold;

    @NotBlank(message = "Plastic is required")
    @Size(max = 20, message = "Plastic cannot be longer than 20 characters")
    private String plastic;

    @NotBlank(message = "Color is required")
    @Size(max = 20, message = "Color cannot be longer than 20 characters")
    private String color;

    @Min(value = 1, message = "Condition must be between 1 and 10")
    @Max(value = 10, message = "Condition must be between 1 and 10")
    private int condition;


    @NotBlank(message = "Description is required. Enter \"N/A\" if not needed")
    @Size(max = 50, message = "Description has a max length of 50 characters")
    private String description;

    @Valid
    @NotNull(message = "Contact information is required. Enter \"N/A\" if unknown")
    private ContactDTO contact;

    @NotBlank(message = "Found location is required. Enter \"N/A\" if unknown.")
    @Size(max = 50, message = "Found location cannot be longer than 50 characters")
    private String foundAt; // Course where disc was found

    private boolean returned;

    private boolean sold;

    @Positive(message = "MSRP cannot be negative")
    private BigDecimal MSRP;

}
