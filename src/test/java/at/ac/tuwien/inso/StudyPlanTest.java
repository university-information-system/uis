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

import java.math.*;

import static java.util.Arrays.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudyPlanTest {

    private StudyPlan studyPlan1 = new StudyPlan("Bachelor Software and Information Engineering", new EctsDistribution(new BigDecimal(90), new BigDecimal(60), new BigDecimal(30)));
    private StudyPlan studyPlan2 = new StudyPlan("Master Business Informatics", new EctsDistribution(new BigDecimal(30), new BigDecimal(70), new BigDecimal(20)));
    private StudyPlan studyPlan3 = new StudyPlan("Master Computational Intelligence", new EctsDistribution(new BigDecimal(60),new BigDecimal(30),new BigDecimal(30)));

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Before
    public void setUp() {
        studyPlanRepository.save(asList(studyPlan1, studyPlan2, studyPlan3));
    }

    @Test
    public void adminShouldSeeAllStudyPlans() throws Exception {
        mockMvc.perform(
                get("/admin/studyplans").with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("studyPlans", asList(studyPlan1, studyPlan2, studyPlan3))
        );
    }

}
