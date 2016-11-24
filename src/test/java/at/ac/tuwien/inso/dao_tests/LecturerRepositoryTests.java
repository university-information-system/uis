package at.ac.tuwien.inso.dao_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.UisUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LecturerRepositoryTests {

    @Autowired
    private UisUserRepository uisUserRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Test
    public void findLecturerByAccountIdSuccessTest() {

        // create lecturer account
        UserAccount account = new UserAccount("lecturer", "pass", Role.LECTURER);
        Lecturer expectedLecturer = uisUserRepository.save(new Lecturer("l0100011", "Una Walker", "una.walker@uis.at", account));
        Long accountId = account.getId();

        // assert the create lecturer matches with the one found
        Lecturer actualLecturer = lecturerRepository.findLecturerByAccountId(accountId);
        assertEquals(expectedLecturer, actualLecturer);
    }

    @Test
    public void findLecturerByAccountIdFailureTest() {

        // search by some invalid id and make sure null is returned
        Long accountId = new Long(-1);
        Lecturer actualLecturer = lecturerRepository.findLecturerByAccountId(accountId);
        assertNull(actualLecturer);
    }
}
