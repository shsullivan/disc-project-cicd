//package com.sullivan.disc.dto;
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class DiscCreateDTOTest {
//
//    private static Validator validator;
//
//    @BeforeAll
//    public static void init() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
////    private DiscCreateDTO createTestDTO () {
////         return new DiscCreateDTO("Innova", "Wraith", "Star", "Blue", 8,
////                                    "My favorite disc", "John", "Smith",
////                                  "3348675309", "Springfield", 19.99);
////    }
//
//    @Test
//    void testValidDiscCreateDTO() {
//        DiscCreateDTO testDTO = createTestDTO();
//
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertTrue(violations.isEmpty());
//    }
//
//    @Test
//    void testInvalidDiscCreateDTO() {
//        DiscCreateDTO testDTO = new DiscCreateDTO();
//
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertTrue(violations.size() >= 10);
//    }
//
//    @Test
//    void testInvalidManufacturerEntryIsToLong() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setManufacturer("A".repeat(21));
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Manufacturer cannot be longer than 20 characters", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidMoldEntryIsToLong() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setMold("A".repeat(21));
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Mold cannot be longer than 20 characters", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidPlasticEntryIsToLong() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setPlastic("A".repeat(21));
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Plastic cannot be longer than 20 characters", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidColorEntryIsToLong() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setColor("A".repeat(21));
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Color cannot be longer than 20 characters", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidConditionValueIsBelowMinimum() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setCondition(0);
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Condition must be between 1 and 10",  violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidConditionValueIsAboveMaximum() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setCondition(11);
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Condition must be between 1 and 10",  violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidDescriptionEntryIsToLong() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setDescription("A".repeat(51));
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Description has a max length of 50 characters", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidContactFirstNameEntryIsToLong() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setContactFirstName("A".repeat(51));
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Contact first name cannot be longer than 50 characters", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidContactLastNameEntryIsToLong() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setContactLastName("A".repeat(51));
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Contact last name cannot be longer than 50 characters", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidFoundAtEntryIsToLong() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setFoundAt("A".repeat(51));
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("Found location cannot be longer than 50 characters", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    void testInvalidMSRPEntryIsNegative() {
//        DiscCreateDTO testDTO = createTestDTO();
//        testDTO.setMSRP(-1);
//        Set<ConstraintViolation<DiscCreateDTO>> violations = validator.validate(testDTO);
//        assertEquals(1, violations.size());
//        assertEquals("MSRP cannot be negative", violations.iterator().next().getMessage());
//    }
//}
