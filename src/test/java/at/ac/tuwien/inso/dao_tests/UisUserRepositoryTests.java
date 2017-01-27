package at.ac.tuwien.inso.dao_tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.repository.UisUserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UisUserRepositoryTests {

    @Autowired
    private UisUserRepository uisUserRepository;

    private Map<String, UisUser> uisUsers = new HashMap<String, UisUser>() {
        {
            put("lecturer1", new Lecturer("l12345", "lecturer1", "l12345@uis.at"));
            put("lecturer2", new Lecturer("l54321", "lecturer2", "l54321@uis.at"));
            put("student", new Student("s12345", "student", "s12345@uis.at"));
        }
    };

    @Before
    public void setUp() {
        uisUserRepository.save(uisUsers.values());
    }

    @Test
    public void testFindAllMatchingSomeExistingUsersUsingIdentificationNumber() {
        Page<UisUser> foundUsers = uisUserRepository.findAllMatching("12345", null);
        assertTrue(foundUsers.getContent().contains(uisUsers.get("lecturer1")));
        assertTrue(foundUsers.getContent().contains(uisUsers.get("student")));
    }

    @Test
    public void testFindAllMatchingSomeExistingUsersUsingNameIgnoreCase() {
        Page<UisUser> foundUsers = uisUserRepository.findAllMatching("lecTURer", null);
        assertTrue(foundUsers.getContent().contains(uisUsers.get("lecturer1")));
        assertTrue(foundUsers.getContent().contains(uisUsers.get("lecturer2")));
    }

    @Test
    public void testFindAllMatchingSomeExistingUsersUsingEmail() {
        Page<UisUser> foundUsers = uisUserRepository.findAllMatching("s12345@uis.at", null);
        assertTrue(foundUsers.getContent().contains(uisUsers.get("student")));
    }

    @Test
    public void testFindAllMatchingNoContent() {
        Page<UisUser> foundUsers = uisUserRepository.findAllMatching("some_non_existing_content", null);
        assertFalse(foundUsers.hasContent());
    }

    @Test
    public void testExistsForExistingIdentificationNumber() throws Exception {
        Lecturer lecturer1 = (Lecturer) uisUsers.get("lecturer1");
        assertTrue(uisUserRepository.existsByIdentificationNumber(lecturer1.getIdentificationNumber()));
    }

    @Test
    public void testExistsForNonExistingIdentificationNumber() throws Exception {
        assertFalse(uisUserRepository.existsByIdentificationNumber("some_non_existing_id"));
    }
}

