package at.ac.tuwien.inso.integration_tests;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.StudentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentGradesTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    private UserAccount studentUser1 = new UserAccount("student12", "pass", Role.STUDENT);
    private UserAccount student22User = new UserAccount("student22", "pass", Role.STUDENT);
    private Student student12 = new Student("s000001", "Student12", "email@1.com", studentUser1);
    private Student student22 = new Student("s000002", "Student22", "email@2.com", student22User);

    @Before
    public void setUp() {
        studentRepository.save(asList(student12, student22));
    }

    @Test
    public void itShowsEmptyGrades() throws Exception {
        mockMvc.perform(
                get("/student/grades").with(user(studentUser1))
        ).andExpect(
                model().attributeExists("grades")
        );
    }

    @Test
    public void itShowsGrades() throws Exception {
        mockMvc.perform(
                get("/student/grades").with(user(student22User))
        ).andExpect(
                model().attributeExists("grades")
        );
    }
}
