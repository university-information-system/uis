package at.ac.tuwien.inso.integration_tests;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import at.ac.tuwien.inso.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AdminSubjectsTests extends AbstractSubjectsTests {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterRepository semesterRepository;

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
        ase.addLecturers(lecturer1, lecturer2);
        Long id1 = subjectRepository.save(calculus).getId();
        Long id2 = subjectRepository.save(sepm).getId();
        Long id3 = subjectRepository.save(ase).getId();

        // admin should see subject calculus without any lecturers
        mockMvc.perform(
                get("/admin/subjects/"+id1).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subject", calculus)
        ).andExpect(
                model().attribute("lecturers", asList())
        );

        //admin should see subject sepm with lecturer1
        mockMvc.perform(
                get("/admin/subjects/"+id2).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subject", sepm)
        ).andExpect(
                model().attribute("lecturers", asList(lecturer1))
        );

        //admin should see subject ase with lecturer1 and lecturer2
        mockMvc.perform(
                get("/admin/subjects/"+id3).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("subject", ase)
        ).andExpect(
                model().attribute("lecturers", asList(lecturer1, lecturer2))
        );
    }

    @Test
    public void adminCreateSubjectTest() throws Exception {

        String name = "maths";
        BigDecimal ects = new BigDecimal(6.0);

        mockMvc.perform(
                post("/admin/subjects/create")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .param("name", name)
                        .param("ects", ects.toString())
                        .with(csrf())
        );

        Subject subject = subjectRepository.findByNameContainingIgnoreCase(name).get(0);
        assertEquals(name, subject.getName());
        assertEquals(ects, subject.getEcts());

    }

    @Test
    public void adminListSubjectsForPageSearchNullAndPageNumberOneTest() throws Exception {

        mockMvc.perform(
                get("/admin/subjects/page/1")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .param("pageNumber", "1")
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/admin/subjects")
        );

    }

    @Test
    public void adminListSubjectsForPageSearchEmptyTest() throws Exception {

        mockMvc.perform(
                get("/admin/subjects/page/1")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .param("search", "")
                        .param("pageNumber", "1")
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/admin/subjects/page/1")
        );

    }

    @Test
    public void adminListSubjectsForPagePageNumberOneTest() throws Exception {

        mockMvc.perform(
                get("/admin/subjects/page/1")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .param("search", "something")
                        .param("pageNumber", "1")
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/admin/subjects?search=something")
        );

    }



}
