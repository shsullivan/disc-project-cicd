package com.sullivan.disc.controller;

import com.sullivan.disc.dto.MoldDTO;
import com.sullivan.disc.model.Mold;
import com.sullivan.disc.util.CustomDataSourceManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h1>MoldController</h1>
 * @author Shawn Sullivan
 * CEN 3024C - 31774
 * July 18, 2025
 * This controller class acts as an endpoint to allow the GUI to access information in the molds table of the DB so that
 * relevant information can be displayed to the user
 */
@RestController
@RequestMapping("/api/molds")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class MoldController {

    private final CustomDataSourceManager dataSourceManager;

    /**
     * The MoldController constructor is not utilized, but must be declared to accept a CustomDataSourceManager object
     * @param dataSourceManager the custom DB manager instantiated after User DB login information has been provided
     */
    public MoldController(CustomDataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    /**
     * This endpoint facilitates a dropdown in the disc creation screen of the GUI that is filtered based on the
     * manufacturer selected by the user in a previous dropdown
     * @param manufacturerId the manufacturer_id used to narrow down molds available for the user to select
     * @return ResponseEntity sent to the UI to display the relevant molds to the user
     */
    @GetMapping("/by-manufacturer/{manufacturerId}")
    public ResponseEntity<?> getMoldsByManufacturer(@PathVariable int manufacturerId) {
        EntityManagerFactory emf = dataSourceManager.getEntityManagerFactory();
        if (emf == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Database connection has not been initialized.");
        }

        EntityManager em = emf.createEntityManager();
        List<Mold> molds = em.createQuery("SELECT m FROM Mold m WHERE m.manufacturer.manufacturerId = :id", Mold.class)
                .setParameter("id", manufacturerId)
                .getResultList();
        em.close();

        List<MoldDTO> dtos = molds.stream()
                .map(m -> new MoldDTO(m.getMoldID(), m.getManufacturer().getManufacturerId() ,m.getMold()))
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
