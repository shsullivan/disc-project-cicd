package com.sullivan.disc.mapper;



import com.sullivan.disc.dto.*;
import com.sullivan.disc.model.Contact;
import com.sullivan.disc.model.Disc;
import com.sullivan.disc.model.Manufacturer;
import com.sullivan.disc.model.Mold;
import org.springframework.stereotype.Component;

/**
 * <h1>DiscMapper</h1>
 * @author Shawn Sullivan
 * CEN 3024C-31774
 * July 8, 2025
 * The DiscMapper class is utilized by both the DiscService class/layer and DiscController class/layer to transition
 * DTOs provided by the frontend to disc entities that can be recorded in the DB and vice versa.
 */
@Component
public class DiscMapper {

    /**
     * Converts a disc entity into a DTO that can be passed outbound to the UI
     * @param disc disc entity from the DB
     * @return DiscDTO that can be converted to JSON
     */
    public DiscDTO discToDiscDTO(Disc disc) {
        if (disc == null) return null;

        return new DiscDTO(
                disc.getDiscID(),
                new ManufacturerDTO(
                        disc.getManufacturer().getManufacturerId(),
                        disc.getManufacturer().getManufacturer()),
                new MoldDTO(
                        disc.getMold().getMoldID(),
                        disc.getMold().getManufacturer().getManufacturerId(),
                        disc.getMold().getMold()),
                disc.getPlastic(),
                disc.getColor(),
                disc.getCondition(),
                disc.getDescription(),
                new ContactDTO(
                        disc.getContact().getContact_id(),
                        disc.getContact().getFirstName(),
                        disc.getContact().getLastName(),
                        disc.getContact().getPhoneNumber()),
                disc.getFoundAt(),
                disc.isReturned(),
                disc.isSold(),
                disc.getMsrp(),
                disc.getResaleValue()
                );
    }

    /**
     * Converts an inbound DiscCreateDTO to a Disc entity that can be saved to the DB
     * @param dto passed from the GUI
     * @param manufacturer objected created to be attributed to the Disc
     * @param mold objected created to be attributed to the Disc
     * @param contact objected created to be attributed to the Disc
     * @return a disc object that can be saved to the repository
     */
    public Disc discCreateDTOtoDisc(DiscCreateDTO dto, Manufacturer manufacturer, Mold mold, Contact contact) {
        return new Disc(
                null,
                manufacturer,
                mold,
                dto.getPlastic(),
                dto.getColor(),
                dto.getCondition(),
                dto.getDescription(),
                contact,
                dto.getFoundAt(),
                false,  // Returned is set to false by default
                false,          // Sold is set to false by default
                dto.getMSRP(),
                null            // Resale value is calculated at the DB level
        );
    }
}
