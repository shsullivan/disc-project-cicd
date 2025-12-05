package com.sullivan.disc.controller;

import com.sullivan.disc.dto.DiscCreateDTO;
import com.sullivan.disc.dto.DiscDTO;
import com.sullivan.disc.dto.DiscUpdateDTO;
import com.sullivan.disc.dto.ImportResultDTO;
import com.sullivan.disc.model.Disc;
import com.sullivan.disc.service.DiscService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * <h1>DiscController</h1>
 * @author Shawn Sullivan
 * CEN 3024C - 31774
 * July 18, 2025
 * This controller class acts as the main endpoint to allow the GUI to interface with the database backend and includes
 * the majority of the working endpoints to facilitate DB management
 */
@CrossOrigin(origins = {
        "http://localhost:5174",
        "http://localhost:5173"
}) // Vite dev server port
@Slf4j
@RestController
@RequestMapping("/api/discs")
public class DiscController {

    private final DiscService discService;

    /**
     * The disc controller is an endpoint so it is not utilized by another layer in the backend, but is defined to
     * accept and assign a discService object
     * @param discService instance of the DiscService class giving the DiscController access to all methods defined
     * therein
     */
    public DiscController(DiscService discService) {
        this.discService = discService;
    }

    /**
     * Post endpoint for new disc record information provided by the user via the web GUI
     * @param dto a DiscCreateDTO that can be passed to and interpreted by the service layer
     * @return ResponseEntity that the frontend GUI can use to display the created discs information to the
     * user
     */
    @PostMapping
    public ResponseEntity<DiscDTO> createDisc(@RequestBody @Valid DiscCreateDTO dto) {
        DiscDTO createdDTO = discService.createDisc(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDTO);
    }

    /**
     * Get or request endpoint that supplies the UI with all discs records in the repository
     * @return List containing all disc records in DTO form that exist in the repo
     */
    @GetMapping
    public List<DiscDTO> getAllDiscs() {

        long start = System.currentTimeMillis();
        log.info("GET /api/discs request received");

        try{
            List<DiscDTO> discDTOs = discService.getAllDiscs();

            long duration = System.currentTimeMillis() - start;
            log.info("GET /api/discs request succesful in {}ms, count: {}", duration, discDTOs.size());

            return discDTOs;
        }
        catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("GET /api/discs request failed after {}ms: {}", duration, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get endpoint that supplies the UI with all discs records in the repository that have "1" in the "returned" field
     * @return List of all returned discs to be displayed in the GUI
     */
    @GetMapping("/returned")
    public List<DiscDTO> getAllReturnedDiscs() {
        return discService.getAllReturned();
    }

    /**
     * Get endpoint that supplies the UI with all discs records in the repository that have "1" in the "sold" field
     * @return List of all sold discs to be displayed in the GUI
     */
    @GetMapping("/sold")
    public List<DiscDTO> getAllSoldDiscs() {
        return discService.getAllSold();
    }

    /**
     * get endpoint that supplies the UI with information for a specific disc based on disc_id
     * @param id provided by the user VIA the UI
     * @return ResponseEntity containing record information for the disc with the specified disc_id
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiscDTO> getDiscById(@PathVariable int id) {
        Optional<DiscDTO> disc = discService.findByID(id);
        return disc.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * get endpoint that supplies the UI with information for a specific disc based on Contact table last_name
     * @param lastName provided by the user VIA the UI
     * @return List containing records information for the discs with the specified contact last_name
     */
    @GetMapping("/search/lastname")
    public List<DiscDTO> findByLastName(@RequestParam String lastName) {
        return discService.findByLastName(lastName);
    }

    /**
     * get endpoint that supplies the UI with information for a specific disc based on Contact table phone_number
     * @param phone provided by the user VIA the UI
     * @return List containing records information for the discs with the specified contact phone_number
     */
    @GetMapping("/search/phone")
    public List<DiscDTO> findByPhone(@RequestParam String phone) {
        return discService.findByPhoneNumber(phone);
    }

    /**
     * Endpoint to receive requests from the UI to delete disc from the DB
     * @param id the ID of the disc to be deleted provided by the user via the UI
     * @return ResponseEntity signalling action completed to the UI
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscById(@PathVariable int id) {
        return discService.deleteDisc(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Endpoint to post uploaded file provided by the user via the GUI and pass to service layer for processing
     * @param file file information sent by the UI
     * @return ResponseEntity to display import results to the user via the GUI
     * @throws IOException if the file is invalid or corrupted
     */
    @PostMapping("/import")
    public ResponseEntity<ImportResultDTO> importFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        ImportResultDTO resultDTO = discService.importFromTextFile(file);
        return ResponseEntity.ok(resultDTO);
    }

    /**
     * Endpoint for a put method sent by the frontend web UI containing information to update an existing disc record.
     * @param id id corresponding to a disc_id in the Discs DB table
     * @param dto data transfer object containing al updated information for existing disc record
     * @return ResponseEntity that allows the GUI to display updated information to the UI
     */
    @PutMapping("/{id}")
    public ResponseEntity<DiscDTO> updateDisc(@PathVariable Integer id, @RequestBody @Valid DiscUpdateDTO dto) {
        return discService.updateDisc(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
