package at.ac.tuwien.inso;

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

import static java.util.Arrays.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
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
