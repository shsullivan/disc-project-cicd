package com.sullivan.disc.controller;

import com.sullivan.disc.dto.ManufacturerDTO;
import com.sullivan.disc.model.Manufacturer;
import com.sullivan.disc.util.CustomDataSourceManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <h1>ManufacturerController</h1>
 * @author Shawn Sullivan
 * CEN 3024C - 31774
 * July 18, 2025
 * This controller class acts as an endpoint to allow the GUI to access information in the manufacturers table of the
 * DB so that relevant information can be displayed to the user
 */
@RestController
@RequestMapping("/api/manufacturers")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ManufacturerController {

    private final CustomDataSourceManager dataSourceManager;

    /**
     * The ManufacturerController constructor is not utilized, but must be declared to accept a CustomDataSourceManager
     * object
     * @param dataSourceManager the custom DB manager instantiated after User DB login information has been provided
     */
    public ManufacturerController(CustomDataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    /**
     * endpoint mapped to allow the GUI to display all manufacturers stored in the DB onscreen for the user to select
     * from the dropdown menu
     * @return ResponseEntity containing JSON information to be interpreted by the GUI and displayed to the user.
     */
    @GetMapping
    public ResponseEntity<?> getAllManufacturers() {
        EntityManagerFactory emf = dataSourceManager.getEntityManagerFactory();
        if (emf == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Database connection has not been initialized.");
        }

        EntityManager em = emf.createEntityManager();
        List<Manufacturer> manufacturers = em.createQuery("SELECT m FROM Manufacturer m", Manufacturer.class)
                .getResultList();
        em.close();

        List<ManufacturerDTO> dtos = manufacturers.stream()
                .map(m -> new ManufacturerDTO(m.getManufacturerId(), m.getManufacturer()))
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
