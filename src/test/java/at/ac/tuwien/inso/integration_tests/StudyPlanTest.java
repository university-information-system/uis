package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.StudyPlanService;

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
import java.util.*;
import java.util.stream.*;

import static java.util.Arrays.*;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudyPlanTest {

    @Autowired
    MockMvc mockMvc;
    private StudyPlan studyPlan1 = new StudyPlan("Bachelor Software and Information Engineering", new EctsDistribution(new BigDecimal(90), new BigDecimal(60), new BigDecimal(30)));
    private StudyPlan studyPlan2 = new StudyPlan("Master Business Informatics", new EctsDistribution(new BigDecimal(30), new BigDecimal(70), new BigDecimal(20)));
    private StudyPlan studyPlan3 = new StudyPlan("Master Computational Intelligence", new EctsDistribution(new BigDecimal(60),new BigDecimal(30),new BigDecimal(30)));
    private List<Subject> subjects;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Before
    public void setUp() {
        Iterable<Subject> subjects = subjectRepository.save(asList(
                new Subject("Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik", new BigDecimal(3.0)),
                new Subject("Software Engineering and Project Management", new BigDecimal(6.0)),
                new Subject("Advanced Software Engineering", new BigDecimal(6.0)),
                new Subject("Digital forensics", new BigDecimal(3.0)),
                new Subject("Model Engineering", new BigDecimal(6.0)),
                new Subject("Formale Methoden", new BigDecimal(6.0)),
                new Subject("Datenbanksysteme", new BigDecimal(6.0)),
                new Subject("Verteile Systeme", new BigDecimal(3.0))
        ));
        this.subjects = StreamSupport.stream(subjects.spliterator(), false).collect(Collectors.toList());

    }

    @Test
    public void adminShouldSeeAllStudyPlans() throws Exception {

        // given study plans
        studyPlanRepository.save(asList(studyPlan1, studyPlan2, studyPlan3));

        // the admin should see them all
        mockMvc.perform(
                get("/admin/studyplans").with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("studyPlans", asList(studyPlan1, studyPlan2, studyPlan3))
        );
    }

    @Test()
    public void adminShouldSeeDetailsOfStudyPlan() throws Exception {

        // given subjects in a study plan
        StudyPlan studyPlan = studyPlanRepository.save(studyPlan1);
        SubjectForStudyPlan s1 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(0), studyPlan, true, 1));
        SubjectForStudyPlan s2 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(1), studyPlan, true, 1));
        SubjectForStudyPlan s3 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(2), studyPlan, true, 2));
        SubjectForStudyPlan s4 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(3), studyPlan, false, 3));
        SubjectForStudyPlan s5 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(4), studyPlan, false, 2));
        SubjectForStudyPlan s6 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(5), studyPlan, true, 2));

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

        // error messages should appear
        createStudyPlan(form)
                .andExpect(view().name("admin/create-studyplan"))
                .andExpect(model().attributeHasFieldErrors("createStudyPlanForm", "name"));

        // and nothing should be persisted
        List<StudyPlan> allStudyPlans = StreamSupport.stream(studyPlanRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertFalse(allStudyPlans.contains(wrongStudyPlan));
    }

    @Test
    public void studyPlanFailureCreationEctsNegativeTest() throws Exception {

        // when the form is filled not correctly
        CreateStudyPlanForm form = new CreateStudyPlanForm("Bachelor SE", new BigDecimal(-120.0), new BigDecimal(-30.0), new BigDecimal(-30.0));
        StudyPlan wrongStudyPlan = form.toStudyPlan();

        // error messages should appear
        createStudyPlan(form)
                .andExpect(view().name("admin/create-studyplan"))
                .andExpect(model().attributeHasFieldErrors("createStudyPlanForm", "mandatory", "optional", "freeChoice"));

        // and nothing should be persisted
        List<StudyPlan> allStudyPlans = StreamSupport.stream(studyPlanRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertFalse(allStudyPlans.contains(wrongStudyPlan));
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
                .map(s -> subjectRepository.findSubjectById(s.getSubject().getId())).collect(Collectors.toList());

        assertTrue(usedSubjects.contains(subject));
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
    
    /**
     * @author m.pazourek
     * Tests if a StudyPlan gets disabled properly
     * @throws Exception 
     */
    @Test
    public void disableStudyPlanTest() throws Exception{
      StudyPlan studyPlan = studyPlanRepository.save(studyPlan1);
      mockMvc.perform(
          get("/admin/studyplans/disable/")
          .param("id", studyPlan.getId().toString())
          .with(user("admin").roles(Role.ADMIN.name()))
          ).andExpect(    
              (redirectedUrl("/admin/studyplans"))
              ).andExpect(it -> {
                StudyPlan s = studyPlanRepository.findOne(studyPlan.getId());
                assertFalse(s.isEnabled());
              });

    }
    
    /**
     * @author m.pazourek
     * Tests if a Subject can be removed from a study plan and that the browser redirects properly afterwards
     * @throws Exception 
     */
    @Test
    public void removeSubjectFromStudyPlanTest() throws Exception{

      // given subjects in a study plan
      StudyPlan studyPlan = studyPlanRepository.save(studyPlan1);
      SubjectForStudyPlan s1 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(0), studyPlan, true, 1));
      SubjectForStudyPlan s2 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(1), studyPlan, true, 1));

      System.out.println("s1" +s1.getId());
      System.out.println("studyPlan "+studyPlan.getId());
      
      mockMvc.perform(
          get("/admin/studyplans/remove/").with(user("admin").roles("ADMIN"))
          .param("studyPlanId", studyPlan.getId().toString())
          .param("subjectId", s2.getSubject().getId().toString())
          ).andExpect(    
              (redirectedUrl("/admin/studyplans/?id="+studyPlan.getId().toString()))
              ).andExpect(it -> {
                List<SubjectForStudyPlan> list = subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(studyPlan.getId());
                assertFalse(list.contains(s2));
              });
    }

    @Test
    public void studentShouldSeeAllStudyPlans() throws Exception {

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
    public void studentShouldSeeOwnStudyPlans() throws Exception {

        // given a student and study plan registrations
        studyPlanRepository.save(asList(studyPlan1, studyPlan2, studyPlan3));
        UserAccount user =  new UserAccount("caroline", "pass", Role.STUDENT);
        Student s = new Student("s1123960", "Caroline Black", "caroline.black@uis.at", user);
        Semester ws = semesterRepository.save(new Semester("WS2016"));
        s.addStudyplans(new StudyPlanRegistration(studyPlan1, ws), new StudyPlanRegistration(studyPlan3, ws));
        studentRepository.save(s);
        StudyPlanRegistration sReg1 = s.getStudyplans().get(0);
        StudyPlanRegistration sReg3 = s.getStudyplans().get(1);

        // the student should see own study plans
        mockMvc.perform(
                get("/student/my-studyplans").with(user(user))
        ).andExpect(
                model().attribute("studyPlanRegistrations", asList(sReg1, sReg3))
        );

    }

}
