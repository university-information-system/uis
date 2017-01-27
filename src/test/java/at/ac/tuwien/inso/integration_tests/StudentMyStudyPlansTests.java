package at.ac.tuwien.inso.integration_tests;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Mark;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.entity.SubjectType;
import at.ac.tuwien.inso.entity.SubjectWithGrade;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentMyStudyPlansTests extends AbstractStudyPlansTests {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    private Semester ws;
    private List<Course> courses;

    @Before
    public void setUp() {
        super.setUp();
        ws = semesterRepository.save(new Semester(2016, SemesterType.WinterSemester));
        Iterable<Course> courses = courseRepository.save(asList(
                new Course(this.subjects.get(0), ws),
                new Course(this.subjects.get(1), ws),
                new Course(this.subjects.get(2), ws),
                new Course(this.subjects.get(3), ws),
                new Course(this.subjects.get(4), ws),
                new Course(this.subjects.get(5), ws)
        ));
        this.courses = StreamSupport.stream(courses.spliterator(), false).collect(Collectors.toList());
    }

    @Test
    public void studentShouldSeeOwnStudyPlansTest() throws Exception {

        // given a student and study plan registrations
        studyPlanRepository.save(asList(studyPlan1, studyPlan2, studyPlan3));
        UserAccount user =  new UserAccount("caroline", "pass", Role.STUDENT);
        Student s = new Student("s1123960", "Caroline Black", "caroline.black@uis.at", user);
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

    @Test
    public void studentShouldSeeDetailsOfOwnStudyPlansTest() throws Exception {

        // given subjects (in a study plan) and grades of a student
        StudyPlan studyPlan = studyPlanRepository.save(studyPlan2);
        SubjectForStudyPlan s1 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(0), studyPlan, true, 1));
        SubjectForStudyPlan s2 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(1), studyPlan, false, 1));
        SubjectForStudyPlan s3 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(2), studyPlan, true, 2));
        SubjectForStudyPlan s4 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(3), studyPlan, false, 3));

        UserAccount user =  new UserAccount("caroline", "pass", Role.STUDENT);
        Student s = new Student("s1123960", "Caroline Black", "caroline.black@uis.at", user);
        s.addStudyplans(new StudyPlanRegistration(studyPlan, ws));
        studentRepository.save(s);
        Lecturer l = lecturerRepository.save(new Lecturer("l1234563", "lecturer", "email"));

        Grade g1 = gradeRepository.save(new Grade(courses.get(0), l, s, Mark.GOOD));
        Grade g3 = gradeRepository.save(new Grade(courses.get(2), l, s, Mark.FAILED));
        Grade g4 = gradeRepository.save(new Grade(courses.get(3), l, s, Mark.GOOD));
        Grade g5 = gradeRepository.save(new Grade(courses.get(4), l, s, Mark.GOOD));
        Grade g6 = gradeRepository.save(new Grade(courses.get(5), l, s, Mark.FAILED));

        // the student should see details of the study plan, containing separate lists of mandatory and optional subjects and grades
        mockMvc.perform(
                get("/student/my-studyplans").param("id", studyPlan.getId().toString()).with(user(user))
        ).andExpect(
                model().attribute("studyPlan", studyPlan)
        ).andExpect(
                model().attribute("mandatory", asList(new SubjectWithGrade(s1, g1, SubjectType.MANDATORY), new SubjectWithGrade(s3, g3, SubjectType.MANDATORY)))
        ).andExpect(
                model().attribute("optional", asList(new SubjectWithGrade(s2, SubjectType.OPTIONAL), new SubjectWithGrade(s4, g4, SubjectType.OPTIONAL)))
        ).andExpect(
                model().attribute("freeChoice", asList(new SubjectWithGrade(g5, SubjectType.FREE_CHOICE), new SubjectWithGrade(g6, SubjectType.FREE_CHOICE)))
        ).andExpect(
                model().attribute("progressMandatory", 3.0)
        ).andExpect(
                model().attribute("progressOptional", 3.0)
        ).andExpect(
                model().attribute("progressFreeChoice", 6.0)
        );
    }


}
