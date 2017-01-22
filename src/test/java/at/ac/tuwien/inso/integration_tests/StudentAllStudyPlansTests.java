package at.ac.tuwien.inso.integration_tests;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentAllStudyPlansTests extends AbstractStudyPlansTests {

    @Test
    public void studentShouldSeeAllStudyPlansTest() throws Exception {

        // given study plans
        studyPlanRepository.save(asList(studyPlan1, studyPlan2, studyPlan3));

        // the student should see them all
        mockMvc.perform(
                get("/student/all-studyplans").with(user("student").roles("STUDENT"))
        ).andExpect(
                model().attribute("studyPlans", asList(studyPlan1, studyPlan2, studyPlan3))
        );
    }

    @Test
    public void studentShouldSeeDetailsOfStudyPlanInAllStudyPlansTest() throws Exception {

        // given subjects in a study plan

        // the student should see details of the study plan, containing separate lists of mandatory and optional subjects
        mockMvc.perform(
                get("/student/all-studyplans").param("id", studyPlan1.getId().toString()).with(user("student").roles("STUDENT"))
        ).andExpect(
                model().attribute("studyPlan", studyPlan1)
        ).andExpect(
                model().attribute("mandatory", asList(s1, s2, s3, s6))
        ).andExpect(
                model().attribute("optional", asList(s5, s4))
        );
    }
}
