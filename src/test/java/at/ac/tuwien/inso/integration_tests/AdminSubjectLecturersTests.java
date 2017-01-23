package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.repository.UisUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AdminSubjectLecturersTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private UisUserRepository uisUserRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private Lecturer lecturer1 = new Lecturer("l1", "lecturer1", "l1@uis.at");
    private Lecturer lecturer2 = new Lecturer("l2", "lecturer2", "l2@uis.at");
    private Lecturer lecturer3 = new Lecturer("l3", "lecturer3", "l3@uis.at");
    private Subject ase = new Subject("ase", new BigDecimal(3.0));

    @Before
    public void setUp() {
        uisUserRepository.save(asList(lecturer1, lecturer2, lecturer3));
        ase.addLecturers(lecturer1);
        subjectRepository.save(ase);
    }

    @Test
    public void addLecturerToSubjectTest() {
        //TODO
    }

    @Test
    public void removeLecturerFromSubjectTest() throws Exception {

        // given 3 lecturers, where lecturer1 is a already a lecturer of the subject ase

        // when removing lecturer1 from subject ase
        mockMvc.perform(
                post("/admin/subjects/" + ase.getId() + "/lecturers/"+ lecturer1.getId() +"/delete")
                        .param("subjectId", ase.getId().toString())
                        .param("lecturerId", lecturer1.getId().toString())
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
        ).
        andExpect(
                redirectedUrl("/admin/subjects/" + ase.getId().toString())
        );

        // lecturer1 shouldn't be part of ase anymore
        assertEquals(asList(), ase.getLecturers());
    }

    @Test
    public void removeLecturerFromASubjectForWhichHeAlreadyDoesNotExistTest() throws Exception {

        // given 3 lecturers, where lecturer1 is a already a lecturer of the subject ase

        // when removing lecturer2 from subject ase
        mockMvc.perform(
                post("/admin/subjects/" + ase.getId() + "/lecturers/"+ lecturer2.getId() +"/delete")
                        .param("subjectId", ase.getId().toString())
                        .param("lecturerId", lecturer2.getId().toString())
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
        ).
                andExpect(
                        redirectedUrl("/admin/subjects/" + ase.getId().toString())
                );

        // nothing should change
        assertEquals(asList(lecturer1), ase.getLecturers());
    }

    @Test
    public void availableLecturersJsonTest() throws Exception {

        // given 3 lecturers, where lecturer1 is a already a lecturer of the subject ase

        // when searching for "lecturer"
        MvcResult result =  mockMvc.perform(
                get("/admin/subjects/"+ase.getId() +"/availableLecturers.json")
                        .with(user("admin").roles("ADMIN"))
                        .param("id", ase.getId().toString())
                        .param("search", "lecturer")
        ).andExpect((status().isOk())
        ).andReturn();

        // the other 2 lecturers should be available for ase
        assertTrue(result.getResponse().getContentAsString().contains("lecturer2"));
        assertTrue(result.getResponse().getContentAsString().contains("lecturer3"));
        assertFalse(result.getResponse().getContentAsString().contains("lecturer1"));
    }
}
