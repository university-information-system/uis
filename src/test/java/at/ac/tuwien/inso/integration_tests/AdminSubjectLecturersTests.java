package at.ac.tuwien.inso.integration_tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AdminSubjectLecturersTests {

    @Test
    public void addLecturerToSubjectTest() {
        //TODO
    }

    @Test
    public void removeLecturerFromSubjectTest() {
        //TODO
    }

    @Test
    public void availableLecturersJsonTest() {
        //TODO
    }
}
