package com.sullivan.disc.service;


import com.sullivan.disc.dto.DiscCreateDTO;
import com.sullivan.disc.dto.DiscDTO;
import com.sullivan.disc.dto.DiscUpdateDTO;
import com.sullivan.disc.dto.ImportResultDTO;
import com.sullivan.disc.mapper.DiscMapper;
import com.sullivan.disc.model.Contact;
import com.sullivan.disc.model.Disc;
import com.sullivan.disc.model.Manufacturer;
import com.sullivan.disc.model.Mold;
import com.sullivan.disc.repository.ContactRepository;
import com.sullivan.disc.repository.DiscRepository;
import com.sullivan.disc.repository.ManufacturerRepository;
import com.sullivan.disc.repository.MoldRepository;
import com.sullivan.disc.util.CustomDataSourceManager;
import com.sullivan.disc.util.DiscValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h1>DiscService</h1>
 * @author Shawn Sullivan
 * CEN 3024C-31774
 * July 8, 2025
 * The DiscService class handles all business logic for the DISC app and acts as an intermediary between the repository
 * layer and the controller endpoint.
 * Note: Spring-managed repositories are temporarily unused due to project requirement for dynamic user DB config.
 * Kept in code for future use because I would like to expand on this project and/or use it as a learning tool
 * later.
 */
@Service
public class DiscService {

    //private final DiscRepository discRepository;
    //private final ContactRepository contactRepository;
    //private final ManufacturerRepository manufacturerRepository;
    //private final MoldRepository moldRepository;
    private final DiscMapper discMapper;
    private final CustomDataSourceManager dataSourceManager;

    /**
     * This constructor allows an instance of the DiscService class to be instantiated and utilized in the
     * DiscController class so that DTOs can be passed from the UI through the DiscController to the DiscService and
     * later to the repository
     * @param discMapper instance of the DiscMapper class that handles conversion of DTOs to Disc entities and vice versa
     * @param dataSourceManager a dataSourceManager must be instantiated to allow the DiscService class to pass disc
     * entities to the data repository layer
     */
    public DiscService(DiscMapper discMapper,
                      CustomDataSourceManager dataSourceManager) {
        this.discMapper = discMapper;
        this.dataSourceManager = dataSourceManager;
    }

    private EntityManager getEntityManager() {
        return dataSourceManager.getEntityManagerFactory().createEntityManager();
    }

    /**
     * Query for getAllReturned and getAllSold is repetitive so created a method to call to increase code readability
     * @param attribute is either "returned" or "sold" and allows the method to create a custom DB query for the desired
     * attribute
     * @return  List the method searches the database for matching records and returns them as a list of
     * DiscDTOs to be passed to the UI via the controller layer and displayed to the user
     */
    private List<DiscDTO> getDiscByBooleanAttribute(String attribute) {
        EntityManager em = getEntityManager();
        String jpql = """
                SELECT d FROM Disc d
                JOIN FETCH d.contact
                JOIN FETCH d.manufacturer
                JOIN FETCH d.mold
                WHERE d.%s = true
                """.formatted(attribute);

        List<Disc> discs = em.createQuery(jpql, Disc.class).getResultList();
        return discs.stream().map(discMapper::discToDiscDTO).collect(Collectors.toList());
    }

    /**
     * This method takes the information contained in a DiscCreate DTO passed from the frontend, and processes it to
     * create a new Disc record in the DB as well as a new contact record if one does not already exist for the provided
     * contact entity
     * @param dto is a data transfer object created from json information passed by the UI to the DiscController class
     * @return DiscDTO that is converted by the DiscController to information that can be displayed on the UI
     */
    public DiscDTO createDisc(DiscCreateDTO dto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        // Check if contact already exists in DB if not, create
        try {
            TypedQuery<Contact> contactQuery = em.createQuery(
                    "SELECT c FROM Contact c WHERE c.firstName = :firstName " +
                            "AND c.lastName = :lastName " +
                            "AND c.phoneNumber = :phoneNumber", Contact.class
            );
            contactQuery.setParameter("firstName", dto.getContact().getFirstName());
            contactQuery.setParameter("lastName", dto.getContact().getLastName());
            contactQuery.setParameter("phoneNumber", dto.getContact().getPhone());
            List<Contact> contacts = contactQuery.getResultList();
            Contact contact = contacts.isEmpty() ? new Contact(null,
                    dto.getContact().getFirstName(),
                    dto.getContact().getLastName(),
                    dto.getContact().getPhone()) : contacts.get(0);
            if (contact.getContact_id() == null) {
                em.persist(contact);
            }

            Manufacturer manufacturer = em.find(Manufacturer.class, dto.getManufacturer().getManufacturerId());
            Mold mold = em.find(Mold.class, dto.getMold().getMoldId());

            Disc disc = discMapper.discCreateDTOtoDisc(dto, manufacturer, mold, contact);
            em.persist(disc);

            tx.commit();
            return discMapper.discToDiscDTO(disc);
        }
        catch (Exception e) {
            tx.rollback();
            throw new RuntimeException("Failed to create disc.", e);
        }
        finally {
            em.close();
        }
    }
//        Optional<Contact> existingContact = contactRepository.findByFirstNameAndLastNameAndPhoneNumber(
//                dto.getContact().getFirstName(),
//                dto.getContact().getLastName(),
//                dto.getContact().getPhone()
//        );
//        Contact contact = existingContact.orElseGet(() -> {
//            Contact newContact = new Contact();
//            newContact.setFirstName(dto.getContact().getFirstName());
//            newContact.setLastName(dto.getContact().getLastName());
//            newContact.setPhoneNumber(dto.getContact().getPhone());
//            return contactRepository.save(newContact);
//        });
//
//        // Retrieve manufacturer and mold by ID sent from UI
//        Manufacturer manufacturer = manufacturerRepository.findById(dto.getManufacturer().getManufacturerId()).get();
//        Mold mold = moldRepository.findById(dto.getMold().getMoldId()).get();
//
//        // Convert DTO and other objects to Disc object
//        Disc disc = discMapper.discCreateDTOtoDisc(dto, manufacturer, mold, contact);
//        discRepository.save(disc);
//        return discMapper.discToDiscDTO(disc);
//    }

    /**
     * This method deletes a Disc record from the database permanently.
     * @param id corresponds to a disc_id record field in the database that is then deleted from the DB
     * @return boolean the boolean value is sent to the DiscController to then signal to the UI to display a success or
     * failure message
     */
    public boolean deleteDisc(Integer id) {
            EntityManager em = getEntityManager();
            EntityTransaction tx = em.getTransaction();
            Disc disc = em.find(Disc.class, id);
            if (disc == null) {
                em.close();
                return false;
            }
            tx.begin();
            em.remove(disc);
            tx.commit();
            return true;
        }
//        if (discRepository.existsById(id)) {
//            discRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }

    /**
     * Query database for all disc records then return a list of DiscDTOs to supply to frontend
     * @return List that can be translated to json that the UI can read and display
     */
    public List<DiscDTO> getAllDiscs() {
        EntityManager em = getEntityManager();
        List<Disc> discs = em.createQuery("""
                SELECT c FROM Disc c
                JOIN FETCH c.contact
                JOIN FETCH c.manufacturer
                JOIN FETCH c.mold""", Disc.class).getResultList();
        em.close();
        return discs.stream().map(discMapper::discToDiscDTO).collect(Collectors.toList());
    }
//        return discRepository.findAll()
//                             .stream()
//                             .map(discMapper::discToDiscDTO)
//                             .collect(Collectors.toList());
//    }

    /**
     * Query database for all discs marked returned == true. Utilizes getDiscByBooleanAttribute helper method and
     * allows the user to track returned discs at the UI level
     * @return List that can be interpreted by the UI and displayed to the user
     */
    public List<DiscDTO> getAllReturned() {
        return getDiscByBooleanAttribute("returned");
    }
//        return discRepository.findByReturnedTrue()
//                             .stream()
//                             .map(discMapper::discToDiscDTO)
//                             .collect(Collectors.toList());
//    }

    /**
     * Query database for all discs marked sold == true. Utilizes getDiscByBooleanAttribute helper and allows user to
     * track sold discs at the UI level
     * @return List that can be converted to JSON and displayed by the frontend UI
     */
    public List<DiscDTO> getAllSold() {
        return getDiscByBooleanAttribute("sold");
    }
//        return discRepository.findBySoldTrue()
//                             .stream()
//                             .map(discMapper::discToDiscDTO)
//                             .collect(Collectors.toList());
//    }

    /**
     * Query database for a specific disc record with the provided disc_id returns an Optional incaseID does not exist
     * @param id is provided by the user on the frontend and should correspond with a disc_id record attribute
     * @return Optional returned to account for the case that a record does not exist.
     */
    public Optional<DiscDTO> findByID(Integer id) {
        EntityManager em = getEntityManager();
        Disc disc = em.find(Disc.class, id);
        em.close();
        return Optional.ofNullable(disc).map(discMapper::discToDiscDTO);
    }
//        return discRepository.findById(id).map(discMapper::discToDiscDTO);
//    }

    /**
     * Discs typically have a persons name sharpied on them at a minimum. This allows the user to easily locate disc
     * records associated with a particular last name.
     * @param lastName corresponds with a last name in the last_name column of the Contact DB table
     * @return List that can then be translated to JSON and displayed to the user on the frontend
     */
    public List<DiscDTO> findByLastName(String lastName) {
        EntityManager em = getEntityManager();
        List<Disc> discs = em.createQuery("SELECT c FROM Disc c WHERE c.contact.lastName = :lastName", Disc.class)
                .setParameter("lastName", lastName)
                .getResultList();
        em.close();
        return discs.stream().map(discMapper::discToDiscDTO).toList();
    }
//        return discRepository.findByContact_LastName(lastName).stream().map(discMapper::discToDiscDTO)
//                .collect(Collectors.toList());
//    }

    /**
     * Discs sometimes have a persons phone info to make contacting them for return easier. This allows the user to
     * easily locate all discs associated with a particular contact phone number.
     * @param phoneNumber should correspond with a phone number in the Contact table of the DB
     * @return List that can then be converted to JSON by the controller and sent to the UI to display onscreen
     */
    public List<DiscDTO> findByPhoneNumber(String phoneNumber) {
        EntityManager em = getEntityManager();
        List<Disc> discs = em.createQuery("SELECT c FROM Disc c WHERE c.contact.phoneNumber = :phone", Disc.class)
                .setParameter("phone", phoneNumber)
                .getResultList();
        em.close();
        return discs.stream().map(discMapper::discToDiscDTO).toList();
    }
//        return discRepository.findByContact_PhoneNumber(phoneNumber).stream().map(discMapper::discToDiscDTO)
//                .collect(Collectors.toList());
//    }


    /**
     * Entering each disc record manually can be tedious. On the frontend the user has the option to upload a .txt file
     * with values separated by "-" characters. This method then parses those values into their associated Disc entity
     * attributes, validates them, and attempts to create a new Disc record in the DB using those validated attributes
     * @param file the file provided by the user via the UI and passed by the controller
     * @return ImportResultDTO is a specialty data transfer object that is interpreted by the UI to display whether the
     * disc record creation was successful or failed for each line of the .txt file
     * @throws IOException incase the file provided is corrupted on invalid
     */
    public ImportResultDTO importFromTextFile(MultipartFile file) throws IOException {
        List<String> successes = new ArrayList<>();
        List<String> failures = new ArrayList<>();

        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] attributes = line.split("-");

                if (attributes.length != 13) {
                    failures.add("Line " + lineCount + ": Wrong number of attributes.");
                    continue;
                }

                try {
                    String manufacturerName = DiscValidator.validateManufacturer(attributes[0].trim());
                    String moldName = DiscValidator.validateMold(attributes[1].trim());
                    String plastic = DiscValidator.validatePlastic(attributes[2].trim());
                    String color = DiscValidator.validateColor(attributes[3].trim());
                    int condition =  DiscValidator.validateCondition(attributes[4].trim());
                    String description = DiscValidator.validateDescription(attributes[5].trim());
                    String contactFirstName = DiscValidator.validateContactName(attributes[6].trim());
                    String contactLastName = DiscValidator.validateContactName(attributes[7].trim());
                    String contactPhone = DiscValidator.validateContactPhone(attributes[8].trim());
                    String foundAt = DiscValidator.validateFoundAt(attributes[9].trim());
                    boolean returned = DiscValidator.validateBooleanInput(attributes[10].trim(), "Returned");
                    boolean sold = DiscValidator.validateBooleanInput(attributes[11].trim(), "Sold");
                    BigDecimal MSRP = DiscValidator.validatePositiveBigDecimal(attributes[12].trim(), "MSRP");

                    // New custom manager code to validate MFR, Mold, Contact
                    TypedQuery<Manufacturer> mfQuery = em.createQuery(
                            "SELECT m FROM Manufacturer m WHERE m.manufacturer = :manufacturer", Manufacturer.class);
                    mfQuery.setParameter("manufacturer", manufacturerName);
                    Manufacturer manufacturer = mfQuery.getResultList().stream().findFirst().orElseThrow(() ->
                            new RuntimeException("Manufacturer " + manufacturerName + " not found"));

                    TypedQuery<Mold> moldQuery = em.createQuery(
                            "SELECT m FROM Mold m WHERE m.mold = :mold AND m.manufacturer = :manufacturer", Mold.class);
                    moldQuery.setParameter("mold", moldName);
                    moldQuery.setParameter("manufacturer", manufacturer);
                    Mold mold = moldQuery.getResultStream().findFirst().orElseThrow(() ->
                            new RuntimeException("Mold " + moldName + " not found"));

                    TypedQuery<Contact> contactQuery = em.createQuery(
                            "SELECT c FROM Contact c WHERE c.firstName = :firstName " +
                                    "AND c.lastName = :lastName " +
                                    "AND c.phoneNumber = :phoneNumber", Contact.class);
                    contactQuery.setParameter("firstName", contactFirstName);
                    contactQuery.setParameter("lastName", contactLastName);
                    contactQuery.setParameter("phoneNumber", contactPhone);
                    Contact contact = contactQuery.getResultStream()
                            .findFirst()
                            .orElseGet(() -> {
                                Contact c = new Contact(null, contactFirstName, contactLastName, contactPhone);
                                em.persist(c);
                                return c;
                            });

    //                // Validate and assign manufacturer
    //                Manufacturer manufacturer = manufacturerRepository.findByManufacturer(manufacturerName)
    //                        .orElseThrow(() -> new RuntimeException("Manufacturer " + manufacturerName + " not found"));
    //
    //                // Validate and assign mold
    //                Mold mold = moldRepository.findByMoldAndManufacturer(moldName, manufacturer)
    //                        .orElseThrow(() -> new RuntimeException("Mold " + moldName + " not found"));
    //
    //                // Validate and assign contact information
    //                Optional<Contact> existingContact = contactRepository.findByFirstNameAndLastNameAndPhoneNumber(
    //                        contactFirstName,
    //                        contactLastName,
    //                        contactPhone
    //                );
    //                Contact contact = existingContact.orElseGet(() ->
    //                    contactRepository.save( new Contact(
    //                            null,
    //                            contactFirstName,
    //                            contactLastName,
    //                            contactPhone
    //                    )));
                    // Create disc from inputs
                    Disc disc = new Disc(null,
                                         manufacturer,
                                         mold,
                                         plastic,
                                         color,
                                         condition,
                                         description,
                                         contact,
                                         foundAt,
                                         returned,
                                         sold,
                                         MSRP,
                                        null);
                    em.persist(disc);
    //                discRepository.save(disc);
                    successes.add("Line " + lineCount + ": Successfully imported. Disc: " + disc.getDiscID() + " created.");
                }
                catch (IllegalArgumentException e) {
                    failures.add("Line " + lineCount + ": " + e.getMessage());
                }
            }
            tx.commit();
        }
        catch (Exception e) {
        tx.rollback();
        throw new RuntimeException("Failed to import discs: " + e.getMessage(), e);
        }
        finally {
            em.close();
        }
        return new ImportResultDTO(successes, failures);
    }

    /**
     * Users should be able to update information regarding a disc record at any time. This method facilitates the user
     * updating information for discs that already exist in the DB
     * @param id should correspond to a disc_id in the Discs table of the DB
     * @param dto is a specialty DiscUpdateDTO generated from UI post inputs that can then be mapped to Disc entity
     * fields to update a database record.
     * @return Optional to be send back to the UI by the DiscController to confirm that a disc has been updated
     * and reflect the changes
     */
    public Optional<DiscDTO> updateDisc(Integer id, DiscUpdateDTO dto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            // Check if disc exists (should always exist)
            Disc disc = em.find(Disc.class, id);
            if (disc == null) {
                return Optional.empty();
            }

            // Set mold and manufacturer from DTO
            Manufacturer manufacturer = em.find(Manufacturer.class, dto.getManufacturer().getManufacturerId());
            Mold mold = em.find(Mold.class, dto.getMold().getMoldId());

            // Lookup or create given contact
            TypedQuery<Contact> contactQuery = em.createQuery(
                    "SELECT c from Contact c WHERE c.firstName = :firstName " +
                            "AND c.lastName = :lastName " +
                            "AND c.phoneNumber = :phoneNumber", Contact.class
            );
            contactQuery.setParameter("firstName", dto.getContact().getFirstName());
            contactQuery.setParameter("lastName", dto.getContact().getLastName());
            contactQuery.setParameter("phoneNumber", dto.getContact().getPhone());
            List<Contact> contacts = contactQuery.getResultList();
            Contact contact = contacts.isEmpty() ? new Contact(null,
                                                                dto.getContact().getFirstName(),
                                                                dto.getContact().getLastName(),
                                                                dto.getContact().getPhone()) : contacts.get(0);
            if (contact.getContact_id() == null) {
                em.persist(contact);
            }

            // Apply updates to disc
            disc.setManufacturer(manufacturer);
            disc.setMold(mold);
            disc.setPlastic(dto.getPlastic());
            disc.setColor(dto.getColor());
            disc.setCondition(dto.getCondition());
            disc.setDescription(dto.getDescription());
            disc.setContact(contact);
            disc.setFoundAt(dto.getFoundAt());
            disc.setReturned(dto.isReturned());
            disc.setSold(dto.isSold());
            disc.setMsrp(dto.getMSRP());

            em.merge(disc);
            tx.commit();
            return Optional.of(discMapper.discToDiscDTO(disc));
        }
        catch (Exception e) {
            tx.rollback();
            throw new RuntimeException("Failed to update disc.", e);
        }
        finally {
            em.close();
        }
    }
//        return discRepository.findById(id).map(existing -> {
//            // Create and validate manufacturer
//            Manufacturer manufacturer = manufacturerRepository.findById(dto.getManufacturer().getManufacturerId()).get();
//
//            // Create and validate mold
//            Mold mold = moldRepository.findById(dto.getMold().getMoldId()).get();
//
//            // Check or create new contact
//            Optional<Contact> existingContact = contactRepository.findByFirstNameAndLastNameAndPhoneNumber(
//                    dto.getContact().getFirstName(),
//                    dto.getContact().getLastName(),
//                    dto.getContact().getPhone()
//            );
//
//            Contact contact = existingContact.orElseGet(() -> contactRepository.save(
//                    new Contact(null,
//                                dto.getContact().getFirstName(),
//                                dto.getContact().getLastName(),
//                                dto.getContact().getPhone())
//            ));
//
//            // Update all disc fields
//            existing.setManufacturer(manufacturer);
//            existing.setMold(mold);
//            existing.setPlastic(dto.getPlastic());
//            existing.setColor(dto.getColor());
//            existing.setCondition(dto.getCondition());
//            existing.setDescription(dto.getDescription());
//            existing.setContact(contact);
//            existing.setFoundAt(dto.getFoundAt());
//            existing.setReturned(dto.isReturned());
//            existing.setSold(dto.isSold());
//            existing.setMsrp(dto.getMSRP());
//
//            // Save disc
//            return discMapper.discToDiscDTO(discRepository.save(existing));
//        });
//    }
}
