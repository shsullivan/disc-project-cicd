//package com.sullivan.disc.repository;
//
//import com.sullivan.disc.model.Disc;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class DiscRepositoryInMemoryTest {
//
//
//
//    @BeforeEach
//    void setup() {
//
//    }
//
//    private Disc generateTestDisc() {
//        return new Disc(0, "Innova", "Wraith", "Star Color Glow", "Blue", 8,
//                "My favorite disc", "John", "Smith", "3348675309",
//                "Springfield", false, false, 19.99);
//    }
//
//    @Test
//    void TestSaveDiscAndFindByID() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//
//        Optional<Disc> found = discRepo.findById(disc.getDiscID());
//        assertTrue(found.isPresent());
//        assertEquals("Innova", found.get().getManufacturer());
//    }
//
//    @Test
//    void TestFindDiscThatDoesNotExistAndFail() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        Optional<Disc> found = discRepo.findById(999);
//        assertFalse(found.isPresent());
//    }
//
//    @Test
//    void TestFindByContactsLastName() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        List<Disc> result = discRepo.findByContactLastName(disc.getContactLastName());
//        assertTrue(result.contains(disc));
//        assertEquals(disc.getContactLastName(), result.get(0).getContactLastName());
//    }
//
//    @Test
//    void TestFindByContactsLastNameAndFail() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        List<Disc> result = discRepo.findByContactLastName("Taylor");
//        assertFalse(result.contains(disc));
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void TestFindByContactsPhoneNumber() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        List<Disc> result = discRepo.findByContactPhoneNumber(disc.getContactPhone());
//        assertTrue(result.contains(disc));
//        assertEquals(disc.getContactPhone(), result.get(0).getContactPhone());
//    }
//
//    @Test
//    void TestFindByContactsPhoneNumberAndFail() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        List<Disc> result = discRepo.findByContactPhoneNumber("9045555555");
//        assertFalse(result.contains(disc));
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void TestFindAllDiscs() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        List<Disc> result = discRepo.findAll();
//        assertTrue(result.contains(disc));
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void TestFindAllDiscsWhenNoDiscsExist() {
//        List<Disc> result = discRepo.findAll();
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void TestFindReturnedDisc() {
//        Disc disc = generateTestDisc();
//        disc.setReturned(true);
//        discRepo.save(disc);
//        List<Disc> result = discRepo.findReturnedDiscs();
//        assertTrue(result.contains(disc));
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void TestFindReturnedDiscWhenNoReturnedDiscExist() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        List<Disc> result = discRepo.findReturnedDiscs();
//        assertTrue(result.isEmpty());
//        assertFalse(disc.isReturned());
//    }
//
//    @Test
//    void TestFindSoldDisc() {
//        Disc disc = generateTestDisc();
//        disc.setSold(true);
//        discRepo.save(disc);
//        List<Disc> result = discRepo.findSoldDiscs();
//        assertTrue(result.contains(disc));
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void TestFindSoldDiscWhenNoSoldDiscExist() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        List<Disc> result = discRepo.findSoldDiscs();
//        assertTrue(result.isEmpty());
//        assertFalse(disc.isSold());
//    }
//
//    @Test
//    void TestDeleteByID() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean deleted = discRepo.deleteById(disc.getDiscID());
//        assertTrue(deleted);
//        assertFalse(discRepo.findById(disc.getDiscID()).isPresent());
//    }
//
//    @Test
//    void TestDeleteByIDWithIDThatDoesNotExist() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean deleted = discRepo.deleteById(999);
//        assertFalse(deleted);
//    }
//
//    @Test
//    void testMarkAsReturned() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean result = discRepo.markAsReturned(disc.getDiscID());
//        assertTrue(result);
//        assertTrue(discRepo.findById(disc.getDiscID()).get().isReturned());
//    }
//
//    @Test
//    void testMarkAsReturnedWhenDiscIDDoesNotExist() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean result = discRepo.markAsReturned(999);
//        assertFalse(result);
//    }
//
//    @Test
//    void testMarkAsSold() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean result = discRepo.markAsSold(disc.getDiscID());
//        assertTrue(result);
//        assertTrue(discRepo.findById(disc.getDiscID()).get().isSold());
//    }
//
//    @Test
//    void testMarkAsSoldWhenDiscIDDoesNotExist() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean result = discRepo.markAsSold(999);
//        assertFalse(result);
//    }
//
//    @Test
//    void testUpdateContactInformation() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean result = discRepo.updateContactInformation(1, "Jimmy", "Johnson",
//                "1234567890");
//        assertTrue(result);
//        assertEquals("Jimmy", disc.getContactFirstName());
//        assertEquals("Johnson", disc.getContactLastName());
//        assertEquals("1234567890", disc.getContactPhone());
//    }
//
//    @Test
//    void testUpdateContactInformationWhenDiscIDDoesNotExist() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean result = discRepo.updateContactInformation(999, "Jimmy", "Johnson", "1234567890");
//        assertFalse(result);
//        assertEquals("John", disc.getContactFirstName());
//        assertEquals("Smith", disc.getContactLastName());
//        assertEquals("3348675309", disc.getContactPhone());
//    }
//
//    @Test
//    void testUpdateMSRP() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean result = discRepo.updateMSRP(disc.getDiscID(), 24.99);
//        assertTrue(result);
//        assertEquals(24.99, disc.getMsrp(), 0.0001);
//    }
//
//    @Test
//    void testUpdateMSRPWhenDiscIDDoesNotExist() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean result = discRepo.updateMSRP(999, 24.99);
//        assertFalse(result);
//        assertEquals(19.99, disc.getMsrp(), 0.0001);
//    }
//
//    @Test
//    void testUpdateMSRPWithInvalidMSRP() {
//        Disc disc = generateTestDisc();
//        discRepo.save(disc);
//        boolean result = discRepo.updateMSRP(1, 0.0);
//        assertFalse(result);
//        assertEquals(19.99, disc.getMsrp(), 0.0001);
//    }
//}
