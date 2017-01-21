package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.clock.FixedClock;
import at.ac.tuwien.inso.clock.FixedClockListener;
import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
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
@FixedClock
@TestExecutionListeners({FixedClockListener.class, DependencyInjectionTestExecutionListener.class})
public class SemesterTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private SemesterRepository semesterRepository;

    @After
    public void terDown() {
        semesterRepository.deleteAll();
    }

    @Test
    @FixedClock("2016-11-11T11:00:00Z")
    public void itListsAllSemesters() throws Exception {
        // no new semester should be added since it already exists
        Semester ss2016 = semesterRepository.save(new Semester(2016, SemesterType.SummerSemester));
        Semester ws2016 = semesterRepository.save(new Semester(2016, SemesterType.WinterSemester));

        mockMvc.perform(
                get("/admin/semester").with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("allSemesters", asList(ws2016.toDto(), ss2016.toDto()))
        ).andExpect(
                model().attribute("currentSemester", ws2016.toDto())
        );
    }

    @Test
    @FixedClock("2020-11-11T11:00:00Z")
    public void itListsCurrentSemesterAsWS2020() throws Exception {
        SemesterDto expected = new SemesterDto(2020, SemesterType.WinterSemester);

        mockMvc.perform(
                get("/admin/semester").with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("allSemesters", asList(expected))
        ).andExpect(
                model().attribute("currentSemester", expected)
        );
    }

    @Test
    @FixedClock("2019-05-11T11:00:00Z")
    public void itListsCurrentSemesterAsWS2019WithOlderSemesters() throws Exception {

        // 2 older semesters already given
        Semester ss2016 = semesterRepository.save(new Semester(2016, SemesterType.SummerSemester));
        Semester ws2016 = semesterRepository.save(new Semester(2016, SemesterType.WinterSemester));

        SemesterDto expected = new SemesterDto(2019, SemesterType.SummerSemester);

        mockMvc.perform(
                get("/admin/semester").with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("allSemesters", asList(expected, ws2016.toDto(), ss2016.toDto()))
        ).andExpect(
                model().attribute("currentSemester", expected)
        );
    }

}
