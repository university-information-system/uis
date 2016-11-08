package at.ac.tuwien.inso;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.repository.SemesterRepository;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SemestersTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private SemesterRepository semesterRepository;

    @Test
    public void itListsAllSemesters() throws Exception {
        Semester ss2016 = semesterRepository.save(new Semester("SS2016"));
        Semester ws2016 = semesterRepository.save(new Semester("WS2016"));

        mockMvc.perform(
                get("/admin/semester").with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("allSemesters", asList(ws2016, ss2016))
        ).andExpect(
                model().attribute("currentSemester", ws2016)
        );
    }

}
