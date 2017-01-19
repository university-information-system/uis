package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.*;

import java.math.*;

import static java.util.Arrays.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SubjectsTest {

    @Autowired
    MockMvc mockMvc;
    private UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    private Lecturer lecturer1 = new Lecturer("l0001", "Lecturer 1", "email", user1);
    private Lecturer lecturer2 = new Lecturer("l0002", "Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER));
    private Lecturer lecturer3 = new Lecturer("l0003", "Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER));
    private Subject calculus = new Subject("Calculus", new BigDecimal(3.0));
    private Subject sepm = new Subject("SEPM", new BigDecimal(6.0));
    private Subject ase = new Subject("ASE", new BigDecimal(6.0));

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private SemesterRepository semesterRepository;

    @Before
    public void setUp() {
        lecturerRepository.save(lecturer1);
        lecturerRepository.save(lecturer2);
        lecturerRepository.save(lecturer3);
    }

    @Test
    public void adminShouldSeeSubjectsOfFirstPage() throws Exception {

        // when subjects are created
        subjectRepository.save(calculus);
        subjectRepository.save(sepm);
        subjectRepository.save(ase);

        // then the admin should see them all in the first page
        mockMvc.perform(
                get("/admin/subjects").with(user("admin").roles("ADMIN")))
                .andExpect(model().attribute("subjects", asList(calculus, sepm, ase))
        );
    }
    
    @Test
    public void adminShouldBeAbleToDeleteSubjects() throws Exception {

        Subject cal = subjectRepository.save(calculus);
        subjectRepository.save(sepm);
        subjectRepository.save(ase);

        // then the admin should see them all in the first page
        mockMvc.perform(
                post("/admin/subjects/remove/"+cal.getId())
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
        ).andExpect(
            redirectedUrl("/admin/subjects/page/0")
        );
        
        assertFalse(subjectRepository.exists(cal.getId()));
    }
    
    @Test
    public void adminSubjectShouldNotBeRemovedIfItContainsCourses() throws Exception {

        Subject cal = subjectRepository.save(calculus);
        subjectRepository.save(sepm);
        subjectRepository.save(ase);
        
        Semester ws2016 = new Semester(2016, SemesterType.WinterSemester);
        semesterRepository.save(ws2016);
        Course calculusWS2016o = new Course(cal, ws2016);
        
        Course calculusWS2016 = courseRepository.save(calculusWS2016o);
        
        

        // then the admin should see them all in the first page
        mockMvc.perform(
                post("/admin/subjects/remove/"+cal.getId())
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
        ).andExpect(
            redirectedUrl("/admin/subjects")
        );
        
        assertTrue(subjectRepository.exists(cal.getId()));
    }


    @Test
    public void adminShouldSeeSubjectDetails() throws Exception {

        // when subjects are created and assigned to lecturers
        sepm.addLecturers(lecturer1);
        ase.addRequiredSubjects(sepm);
        ase.addLecturers(lecturer1, lecturer2);
        Long id1 = subjectRepository.save(calculus).getId();
        Long id2 = subjectRepository.save(sepm).getId();
        Long id3 = subjectRepository.save(ase).getId();

        // admin should see subject calculus without any lecturers or prerequisites
        mockMvc.perform(
                get("/admin/subjects/"+id1).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subject", calculus)
        ).andExpect(
                model().attribute("lecturers", asList())
        ).andExpect(
                model().attribute("requiredSubjects", asList())
        );

        //admin should see subject sepm with lecturer1 and no required subject sepm
        mockMvc.perform(
                get("/admin/subjects/"+id2).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subject", sepm)
        ).andExpect(
                model().attribute("lecturers", asList(lecturer1))
        ).andExpect(
                model().attribute("requiredSubjects", asList())
        );

        //admin should see subject ase with lecturer1 and lecturer2 and required subject sepm
        mockMvc.perform(
                get("/admin/subjects/"+id3).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subject", ase)
        ).andExpect(
                model().attribute("lecturers", asList(lecturer1, lecturer2))
        ).andExpect(
                model().attribute("requiredSubjects", asList(sepm))
        );
    }

    @Test
    public void lecturersShouldSeeTheirOwnSubjects() throws Exception {

        // when subjects are created and assigned to lecturers
        sepm.addLecturers(lecturer1);
        ase.addRequiredSubjects(sepm);
        ase.addLecturers(lecturer1, lecturer2);
        subjectRepository.save(calculus);
        subjectRepository.save(sepm);
        subjectRepository.save(ase);

        // lecturer1 should see sepm and ase
        mockMvc.perform(
                get("/lecturer/subjects").with(user("lecturer1").roles("LECTURER"))
        ).andExpect(
                model().attribute("ownedSubjects", asList(sepm, ase))
        );

        // lecturer2 should see ase
        mockMvc.perform(
                get("/lecturer/subjects").with(user("lecturer2").roles("LECTURER"))
        ).andExpect(
                model().attribute("ownedSubjects", asList(ase))
        );

        // lecturer3 should see nothing
        mockMvc.perform(
                get("/lecturer/subjects").with(user("lecturer3").roles("LECTURER"))
        ).andExpect(
                model().attribute("ownedSubjects", asList())
        );

    }

}
