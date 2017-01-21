package at.ac.tuwien.inso.integration_tests;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class LecturerStudyPlansTests extends AbstractStudyPlansTests {

    @Test
    public void lecturerShouldSeeAllStudyPlansTest() throws Exception {

        // given study plans
        studyPlanRepository.save(asList(studyPlan1, studyPlan2, studyPlan3));

        // the lecturer should see them all
        mockMvc.perform(
                get("/lecturer/studyplans").with(user("lecturer").roles("LECTURER"))
        ).andExpect(
                model().attribute("studyPlans", asList(studyPlan1, studyPlan2, studyPlan3))
        );
    }

    @Test
    public void lecturerShouldSeeDetailsOfStudyPlanInAllStudyPlansTest() throws Exception {

        // given subjects in a study plan

        // the lecturer should see details of the study plan, containing separate lists of mandatory and optional subjects
        mockMvc.perform(
                get("/lecturer/studyplans").param("id", studyPlan1.getId().toString()).with(user("lecturer").roles("LECTURER"))
        ).andExpect(
                model().attribute("studyPlan", studyPlan1)
        ).andExpect(
                model().attribute("mandatory", asList(s1, s2, s3, s6))
        ).andExpect(
                model().attribute("optional", asList(s5, s4))
        );
    }
}
