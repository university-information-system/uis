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

    private UserAccount studentUser = new UserAccount("student1", "pass", Role.STUDENT);
    private UserAccount student2User = new UserAccount("student2", "pass", Role.STUDENT);
    private Student student1 = new Student("s000001", "Student1", "email", studentUser);
    private Student student2 = new Student("s000002", "Student2", "email", student2User);

    @Before
    public void setUp() {
        studentRepository.save(asList(student1, student2));
    }

    @Test
    public void itShowsEmptyGrades() throws Exception {
        mockMvc.perform(
                get("/student/grades").with(user(studentUser))
        ).andExpect(
                model().attributeExists("grades")
        );
    }

    @Test
    public void itShowsGrades() throws Exception {
        mockMvc.perform(
                get("/student/grades").with(user(student2User))
        ).andExpect(
                model().attributeExists("grades")
        );
    }
}
