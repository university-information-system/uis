package at.ac.tuwien.inso;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
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

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SubjectsTest {

    private UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    private Lecturer lecturer1 = new Lecturer("Lecturer 1", "email", user1);
    private Lecturer lecturer2 = new Lecturer("Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER));
    private Lecturer lecturer3 = new Lecturer("Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER));
    private Subject calculus = new Subject("Calculus", new BigDecimal(3.0));
    private Subject sepm = new Subject("SEPM", new BigDecimal(6.0));
    private Subject ase = new Subject("ASE", new BigDecimal(6.0));

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;


    @Before
    public void setUp() {
        lecturerRepository.save(lecturer1);
        lecturerRepository.save(lecturer2);
        lecturerRepository.save(lecturer3);
    }

    @Test
    @Transactional
    public void adminShouldSeeAllSubjects() throws Exception {

        // when subjects are created
        subjectRepository.save(calculus);
        subjectRepository.save(sepm);
        subjectRepository.save(ase);

        // then the admin should see them all
        mockMvc.perform(
                get("/admin/subjects").with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subjects", asList(calculus, sepm, ase))
        );
    }

    @Test
    @Transactional
    public void adminShouldSeeSubjectDetails() throws Exception {

        // when subjects are created and assigned to lecturers
        lecturerRepository.save(lecturer1);
        lecturerRepository.save(lecturer2);
        lecturerRepository.save(lecturer3);
        sepm.addLecturers(lecturer1);
        ase.addRequiredSubjects(sepm);
        ase.addLecturers(lecturer1, lecturer2);
        subjectRepository.save(calculus);
        subjectRepository.save(sepm);
        subjectRepository.save(ase);

        System.out.println(ase);

        // admin should see subject calculus without any lecturers or prerequisites
        mockMvc.perform(
                get("/admin/subjects").param("id", String.valueOf(calculus.getId())).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subject", calculus)
        ).andExpect(
                model().attribute("lecturers", asList())
        ).andExpect(
                model().attribute("requiredSubjects", asList())
        );

        //admin should see subject sepm with lecturer1 and no required subject sepm
        mockMvc.perform(
                get("/admin/subjects").param("id", String.valueOf(sepm.getId())).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subject", sepm)
        ).andExpect(
                model().attribute("lecturers", asList(lecturer1))
        ).andExpect(
                model().attribute("requiredSubjects", asList())
        );

        //admin should see subject ase with lecturer1 and lecturer2 and required subject sepm
        mockMvc.perform(
                get("/admin/subjects").param("id", String.valueOf(ase.getId())).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subject", ase)
        ).andExpect(
                model().attribute("lecturers", asList(lecturer1, lecturer2))
        ).andExpect(
                model().attribute("requiredSubjects", asList(sepm))
        );
    }

}
