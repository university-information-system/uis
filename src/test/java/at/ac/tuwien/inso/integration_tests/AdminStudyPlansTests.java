package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.controller.admin.forms.CreateStudyPlanForm;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.StudyPlanRepository;
import at.ac.tuwien.inso.repository.UisUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AdminStudyPlansTests extends AbstractStudyPlansTests {

    @Autowired
    private UisUserRepository uisUserRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudyPlanRepository studyplanRepository;


    @Test
    public void adminShouldSeeAllStudyPlansTest() throws Exception {

        // given study plans
        studyPlanRepository.save(asList(studyPlan1, studyPlan2, studyPlan3));

        // the admin should see them all
        mockMvc.perform(
                get("/admin/studyplans").with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("studyPlans", asList(studyPlan1, studyPlan2, studyPlan3))
        );
    }

    @Test
    public void adminShouldSeeDetailsOfStudyPlanTest() throws Exception {

        // given subjects in a study plan
        StudyPlan studyPlan = studyPlanRepository.save(studyPlan1);

        // the admin should see details of the study plan, containing separate lists of mandatory and optional subjects
        mockMvc.perform(
                get("/admin/studyplans").param("id", studyPlan.getId().toString()).with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("studyPlan", studyPlan)
        ).andExpect(
                model().attribute("mandatory", asList(s1, s2, s3, s6))
        ).andExpect(
                model().attribute("optional", asList(s5, s4))
        );
    }

    @Test
    public void studyPlanSuccessCreationTest() throws Exception {

        // when the form is filled correctly
        CreateStudyPlanForm form = new CreateStudyPlanForm("Bachelor SE", new BigDecimal(120.0), new BigDecimal(30.0), new BigDecimal(30.0));
        StudyPlan expectedStudyPlan = form.toStudyPlan();

        // the created study plan should be seen
        createStudyPlan(form)
                .andExpect(view().name("admin/studyplan-details"))
                .andExpect(model().attribute("studyPlan", expectedStudyPlan));

        // and it should be persisted
        List<StudyPlan> allStudyPlans = StreamSupport.stream(studyPlanRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertTrue(allStudyPlans.contains(expectedStudyPlan));
    }

    @Test
    public void studyPlanFailureCreationNameIsEmptyTest() throws Exception {

        // when the form is filled not correctly
        CreateStudyPlanForm form = new CreateStudyPlanForm("", new BigDecimal(120.0), new BigDecimal(30.0), new BigDecimal(30.0));
        StudyPlan wrongStudyPlan = form.toStudyPlan();

        // nothing should be persisted
        List<StudyPlan> allStudyPlans = StreamSupport.stream(studyPlanRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertFalse(allStudyPlans.contains(wrongStudyPlan));
    }

    @Test
    public void studyPlanFailureCreationEctsNegativeTest() throws Exception {

        // when the form is filled not correctly
        CreateStudyPlanForm form = new CreateStudyPlanForm("Bachelor SE", new BigDecimal(-120.0), new BigDecimal(-30.0), new BigDecimal(-30.0));
        StudyPlan wrongStudyPlan = form.toStudyPlan();

        // nothing should be persisted
        List<StudyPlan> allStudyPlans = StreamSupport.stream(studyPlanRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertFalse(allStudyPlans.contains(wrongStudyPlan));
    }

    @Test
    public void disableStudyPlanTest() throws Exception{
        StudyPlan studyPlan = studyPlanRepository.save(studyPlan1);
        mockMvc.perform(
                post("/admin/studyplans/disable/")
                        .param("id", studyPlan.getId().toString())
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .with(csrf())
        ).andExpect(
                (redirectedUrl("/admin/studyplans"))
        ).andExpect(it -> {
            StudyPlan s = studyPlanRepository.findOne(studyPlan.getId());
            assertFalse(s.isEnabled());
        });
    }

    @Test
    public void addSubjectToStudyPlanSuccessTest() throws Exception {

        //given study plan and a subject which is not part of it
        StudyPlan studyPlan = studyPlanRepository.save(studyPlan1);
        Subject subject = subjectRepository.save(new Subject("Operating Systems", new BigDecimal(4.0)));

        //when a mandatory subject is added to the study plan
        mockMvc.perform(
                post("/admin/studyplans/addSubject")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .param("subjectId", subject.getId().toString())
                        .param("studyPlanId", studyPlan.getId().toString())
                        .param("semester", "1")
                        .param("mandatory", "true")
                        .with(csrf())
        ).andExpect(
                (redirectedUrl("/admin/studyplans/?id="+studyPlan.getId()))
        );

        //the subject should be part of the mandatory subjects of the study plan
        List<SubjectForStudyPlan> subjectsForStudyPlan = StreamSupport
                .stream(subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(studyPlan.getId())
                        .spliterator(), false).collect(Collectors.toList());
        List<Subject> usedSubjects = subjectsForStudyPlan.stream()
                .filter(SubjectForStudyPlan::getMandatory)
                .map(s -> subjectRepository.findById(s.getSubject().getId())).collect(Collectors.toList());

        assertTrue(usedSubjects.contains(subject));
    }

    @Test
    public void addNonExistingSubjectToStudyPlanFailureTest() throws Exception {

        //given study plan and a subject which is already part of it
        Subject subject = new Subject("Operating Systems", new BigDecimal(4.0));
        subject.setId(1337L);
        //studyPlan1.addSubjects(new SubjectForStudyPlan(subject, studyPlan1, true));
        StudyPlan studyPlan = studyPlanRepository.save(studyPlan1);

        //when this mandatory subject is added to the study plan
        mockMvc.perform(
                post("/admin/studyplans/addSubject")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .param("subjectId", subject.getId().toString())
                        .param("studyPlanId", studyPlan.getId().toString())
                        .param("semester", "1")
                        .param("mandatory", "true")
                        .with(csrf())
        ).andExpect(
                (redirectedUrl("/admin/studyplans/?id="+studyPlan.getId()))
        );

        //the subject should not be part of the mandatory subjects of the study plan
        List<SubjectForStudyPlan> subjectsForStudyPlan = StreamSupport
                .stream(subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(studyPlan.getId())
                        .spliterator(), false).collect(Collectors.toList());
        List<Subject> usedSubjects = subjectsForStudyPlan.stream()
                .filter(SubjectForStudyPlan::getMandatory)
                .map(s -> subjectRepository.findById(s.getSubject().getId())).collect(Collectors.toList());

        assertFalse(usedSubjects.contains(subject));
    }

    /**
     * @author m.pazourek
     * Tests if a Subject can be removed from a study plan and that the browser redirects properly afterwards
     * @throws Exception
     */
    @Test
    public void removeSubjectFromStudyPlanTest() throws Exception{

        // given subjects in a study plan
        StudyPlan studyPlan = studyPlanRepository.save(studyPlan2);
        SubjectForStudyPlan s1 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(0), studyPlan, true, 1));
        SubjectForStudyPlan s2 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(1), studyPlan, true, 1));

        mockMvc.perform(
                post("/admin/studyplans/remove/").with(user("admin").roles("ADMIN"))
                        .param("studyPlanId", studyPlan.getId().toString())
                        .param("subjectId", s2.getSubject().getId().toString())
                        .with(csrf())
        ).andExpect(
                (redirectedUrl("/admin/studyplans/?id="+studyPlan.getId().toString()))
        ).andExpect(it -> {
            List<SubjectForStudyPlan> list = subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(studyPlan.getId());
            assertFalse(list.contains(s2));
        });
    }

    @Test
    public void registerStudentToStudyPlanTest() throws Exception {

        // given a student and a study plan
        Student student = uisUserRepository.save(new Student("s12345", "student", "student@uis.at"));

        // when registering this student to the study plan
        mockMvc.perform(
                post("/admin/studyplans/registerStudent").with(user("admin").roles("ADMIN"))
                        .param("studyPlanId", studyPlan1.getId().toString())
                        .param("studentId", student.getId().toString())
                        .with(csrf())
        ).andExpect(
                (redirectedUrl("/admin/users/"+student.getId().toString()))
        ).andExpect(it -> {
            List<StudyPlan> studyPlans = student.getStudyplans().stream().map(StudyPlanRegistration::getStudyplan).collect(Collectors.toList());
            assertTrue(studyPlans.contains(studyPlan1));
        });

    }

    @Test
    public void isStudyPlanAddedToStudent() throws Exception {
        Semester semester = semesterRepository.save(new Semester(2016, SemesterType.WinterSemester));
        Student newStudent = studentRepository.save(new Student("s1123456", "TestUser", "a@c.com"));

        StudyPlan sp = studyplanRepository.save(new StudyPlan("TestStudyPlan", new EctsDistribution(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN)));

        mockMvc.perform(
                post("/admin/studyplans/registerStudent/")
                        .param("studentId", newStudent.getId()+"")
                        .param("studyPlanId", sp.getId()+"")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/admin/users/"+newStudent.getId())
        ).andExpect(it -> {
            Student s = studentRepository.findOne(newStudent.getId());
            assertEquals(sp, s.getStudyplans().get(0).getStudyplan());
        });
    }

    @Test
    public void availableSubjectsForStudyPlanJsonTest() throws Exception {

        // given 3 subjects, where all of them contain the string "Engineering", but one is already part of studyPlan2
        studyPlan1.addSubjects(new SubjectForStudyPlan(subjects.get(1), studyPlan2, true));
        studyPlanRepository.save(studyPlan2);

        // when searching for "Engineering
        MvcResult result =  mockMvc.perform(
                get("/admin/studyplans/json/availableSubjects").with(user("admin").roles("ADMIN"))
                        .param("id", studyPlan2.getId().toString())
                        .param("query", "Engineering")
        ).andExpect((status().isOk())
        ).andReturn();

        // the other 2 subjects should be available for studyPlan2
        assertTrue(result.getResponse().getContentAsString().contains("Advanced Software Engineering"));
        assertTrue(result.getResponse().getContentAsString().contains("Model Engineering"));
        assertFalse(result.getResponse().getContentAsString().contains("Software Engineering and Project Management"));
    }

    private ResultActions createStudyPlan(CreateStudyPlanForm form) throws Exception {
        return mockMvc.perform(
                post("/admin/studyplans/create").with(user("admin").roles(Role.ADMIN.name()))
                        .param("name", form.getName())
                        .param("mandatory", form.getMandatory().toString())
                        .param("optional", form.getOptional().toString())
                        .param("freeChoice", form.getFreeChoice().toString())
                        .with(csrf())
        );
    }
}
