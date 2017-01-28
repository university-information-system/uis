package at.ac.tuwien.inso.integration_tests;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.jboss.aerogear.security.otp.Totp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LecturerCourseDetailsTests {


    UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    Lecturer lecturer1 = new Lecturer("l0001", "Lecturer 1", "email1@uis.at", user1);
    Lecturer lecturer2 = new Lecturer("l0002", "Lecturer 2", "email2@uis.at", new UserAccount("lecturer2", "pass", Role.LECTURER));
    Lecturer lecturer3 = new Lecturer("l0003", "Lecturer 3", "email3@uis.at", new UserAccount("lecturer3", "pass", Role.LECTURER));
    Student student = new Student("st1", "Student", "st@ude.nt", new UserAccount("st1", "pass", Role.STUDENT));
    Semester ss2016 = new Semester(2016, SemesterType.SummerSemester);
    Semester ws2016 = new Semester(2016, SemesterType.WinterSemester);
    Subject calculus = new Subject("Calculus", new BigDecimal(3.0));
    Subject sepm = new Subject("SEPM", new BigDecimal(6.0));
    Subject ase = new Subject("ASE", new BigDecimal(6.0));
    Course sepmSS2016 = new Course(sepm, ss2016);
    Course sepmWS2016 = new Course(sepm, ws2016);
    Course aseWS2016 = new Course(ase, ws2016);
    Course calculusWS2016 = new Course(calculus, ws2016);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Before
    public void setUp() {

        student = studentRepository.save(student);

        lecturer1 = lecturerRepository.save(lecturer1);
        lecturer2 = lecturerRepository.save(lecturer2);
        lecturer3 = lecturerRepository.save(lecturer3);

        semesterRepository.save(ss2016);
        semesterRepository.save(ws2016);

        subjectRepository.save(calculus);
        calculus.addLecturers(lecturer3);
        subjectRepository.save(sepm);
        sepm.addLecturers(lecturer1);
        subjectRepository.save(ase);
        ase.addLecturers(lecturer1, lecturer2);

        sepmSS2016 = courseRepository.save(sepmSS2016);
        sepmWS2016 = courseRepository.save(sepmWS2016);
        aseWS2016 = courseRepository.save(aseWS2016);
        calculusWS2016 = courseRepository.save(calculusWS2016);


        aseWS2016.getStudents().add(student);

        tagRepository.save(asList(
                new Tag("Computer Science"),
                new Tag("Math"),
                new Tag("Fun"),
                new Tag("Easy"),
                new Tag("Difficult")
        ));
    }

    @Test
    public void lecturersShouldSeeTheirIssuedGradesInCourseTest() throws Exception {

        // given course ase held by lecturer1 and lecturer2 and 3 registered students
        Student st2 = new Student("st2", "gradeStudent2", "st2@ude.nt", new UserAccount("gradest2", "pass", Role.STUDENT));
        Student st3 = new Student("st3", "gradeStudent3", "st3@ude.nt", new UserAccount("gradest3", "pass", Role.STUDENT));
        Student st4 = new Student("st4", "gradeStudent3", "st4@ude.nt", new UserAccount("gradest4", "pass", Role.STUDENT));
        studentRepository.save(asList(st2, st3, st4));
        aseWS2016.addStudents(st2, st3, st4);

        // when: lecturer1 issues grades for st1 and st2
        //       lecturer2 issues grades for st3
        Grade gr1 = gradeRepository.save(new Grade(aseWS2016, lecturer1, st2, Mark.EXCELLENT));
        Grade gr2 = gradeRepository.save(new Grade(aseWS2016, lecturer1, st3, Mark.FAILED));
        Grade gr3 = gradeRepository.save(new Grade(aseWS2016, lecturer2, st4, Mark.GOOD));

        // then lecturer1 and lecturer2 should see their issued grades
        mockMvc.perform(
                get("/lecturer/course-details/issued-grades")
                        .param("courseId", aseWS2016.getId().toString())
                        .with(user("lecturer1").roles(Role.LECTURER.name()))
                        .with(csrf())
        ).andExpect(
                model().attribute("course", aseWS2016)
        ).andExpect(
                model().attribute("grades", asList(gr1, gr2))
        );

        mockMvc.perform(
                get("/lecturer/course-details/issued-grades")
                        .param("courseId", aseWS2016.getId().toString())
                        .with(user("lecturer2").roles(Role.LECTURER.name()))
                        .with(csrf())
        ).andExpect(
                model().attribute("course", aseWS2016)
        ).andExpect(
                model().attribute("grades", asList(gr3))
        );

    }

    @Test
    public void lecturersShouldSeeRegisteredStudentsToCourseTest() throws Exception {
        // given lecturer1 and lecturer2 of the sepm course

        // when a student has registered to the sepm course
        sepmSS2016.addStudents(student);

        //lecturer1 should see the registered student
        mockMvc.perform(
                get("/lecturer/course-details/registrations")
                        .param("courseId", sepmSS2016.getId().toString())
                        .with(user("lecturer1").roles(Role.LECTURER.name()))
                        .with(csrf())
        ).andExpect(
                model().attribute("course", sepmSS2016)
        ).andExpect(
                model().attribute("students", asList(student))
        );

        //lecturer2 should see the registered student too
        mockMvc.perform(
                get("/lecturer/course-details/registrations")
                        .param("courseId", sepmSS2016.getId().toString())
                        .with(user("lecturer2").roles(Role.LECTURER.name()))
                        .with(csrf())
        ).andExpect(
                model().attribute("course", sepmSS2016)
        ).andExpect(
                model().attribute("students", asList(student))
        );

    }

    @Test
    public void lecturerShouldSeeFeedback() throws Exception {

        // given course ase held by lecturer1 and students student1 and student2 with grades in ase
        Student st1 = studentRepository.save(new Student("stud1", "gradeStudent2", "st1@ude.nt", new UserAccount("gradestud1", "pass", Role.STUDENT)));
        Student st2 = studentRepository.save(new Student("stud2", "gradeStudent3", "st2@ude.nt", new UserAccount("gradestud2", "pass", Role.STUDENT)));
        Subject ase = subjectRepository.save(new Subject("ASE", new BigDecimal(6.0)));
        Semester ws2016 = semesterRepository.save(new Semester(2016, SemesterType.WinterSemester));
        Course aseWS2016 = courseRepository.save(new Course(ase, ws2016).addStudents(st1,st2));
        gradeRepository.save(new Grade(aseWS2016, lecturer1, st1, Mark.EXCELLENT));
        gradeRepository.save(new Grade(aseWS2016, lecturer1, st2, Mark.GOOD));

        // when student1 and student2 give feedback
        Feedback feedback1 = new Feedback(
                st1,
                aseWS2016,
                Feedback.Type.DISLIKE, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec enim ligula. " +
                "Sed eget posuere tellus. Aenean fermentum maximus tempor. Ut ultricies dapibus nulla vitae mollis. " +
                "Suspendisse a nunc nisi. Sed ut sapien eu odio sodales laoreet eu ac turpis. " +
                "In id sapien id ante sollicitudin consectetur at laoreet mi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Suspendisse quam sem, ornare eget pellentesque sit amet, tincidunt id metus. Sed scelerisque neque sed laoreet elementum. " +
                "Integer eros neque, vulputate a hendrerit at, ullamcorper in orci. Donec sit amet risus hendrerit, hendrerit magna non, dapibus nibh. " +
                "Suspendisse sed est feugiat, dapibus ante non, aliquet neque. Cras magna sapien, pharetra ut ante ut, malesuada hendrerit erat. " +
                "Mauris fringilla mattis dapibus. Nullam iaculis nunc in tortor gravida, id tempor justo elementum.");
        Feedback feedback2 = new Feedback(
                st2,
                aseWS2016,
                Feedback.Type.LIKE, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec enim ligula. " +
                "Sed eget posuere tellus. Aenean fermentum maximus tempor. Ut ultricies dapibus nulla vitae mollis. " +
                "Suspendisse a nunc nisi. Sed ut sapien eu odio sodales laoreet eu ac turpis. " +
                "In id sapien id ante sollicitudin consectetur at laoreet mi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Suspendisse quam sem, ornare eget pellentesque sit amet, tincidunt id metus. Sed scelerisque neque sed laoreet elementum. " +
                "Integer eros neque, vulputate a hendrerit at, ullamcorper in orci. Donec sit amet risus hendrerit, hendrerit magna non, dapibus nibh. " +
                "Suspendisse sed est feugiat, dapibus ante non, aliquet neque. Cras magna sapien, pharetra ut ante ut, malesuada hendrerit erat. " +
                "Mauris fringilla mattis dapibus. Nullam iaculis nunc in tortor gravida, id tempor justo elementum.");

        feedbackRepository.save(asList(feedback1, feedback2));

        // the lecturer should see the feedback
        mockMvc.perform(
                get("/lecturer/course-details/feedback")
                        .param("courseId", aseWS2016.getId().toString())
                        .with(user("lecturer1").roles(Role.LECTURER.name()))
                        .with(csrf())
        ).andExpect(
                model().attribute("course", aseWS2016)
        ).andExpect(
                model().attribute("feedbacks", asList(feedback1, feedback2))
        );

    }

    @Test
    public void lecturerShouldSeeCourseDetailsTest() throws Exception {

        // given course "maths" in study plan "SE" with some tags with "lecturer1"
        Tag tag1 = new Tag("math");
        Tag tag2 = new Tag("calculus");
        tagRepository.save(asList(tag1, tag2));
        Subject maths = subjectRepository.save(new Subject("maths", new BigDecimal(6.0)));
        maths.addLecturers(lecturer1);
        Course mathsCourse =  new Course(maths, ws2016, "some description");
        mathsCourse.setStudentLimits(1);
        mathsCourse.addTags(tag1, tag2);
        courseRepository.save(mathsCourse);
        StudyPlan studyPlan = studyPlanRepository.save(new StudyPlan("SE", new EctsDistribution(new BigDecimal(60.0), new BigDecimal(30.0), new BigDecimal(30.0))));
        SubjectForStudyPlan subjectForStudyPlan = new SubjectForStudyPlan(maths, studyPlan, true);
        studyPlan.addSubjects(subjectForStudyPlan);
        List<SubjectForStudyPlan> expectedSubjectForStudyPlanList = new ArrayList<>();
        expectedSubjectForStudyPlanList.add(subjectForStudyPlan);

        // the lecturer should see the course details
        mockMvc.perform(
                get("/lecturer/course-details")
                        .param("courseId", mathsCourse.getId().toString())
                        .with(user("lecturer1").roles(Role.LECTURER.name()))
                        .with(csrf())
        ).andExpect(
                model().attribute("course", mathsCourse)
        ).andExpect(
                model().attribute("studyPlans", expectedSubjectForStudyPlanList)
        );
    }

    @Test
    public void issueGradeSuccessTest() throws Exception {

        // given "student" and "lecturer1" with the course "ase"
        Totp totp = new Totp(lecturer1.getTwoFactorSecret());

        // when "lecturer1" issues a grade for "student"
        mockMvc.perform(
                post("/lecturer/course-details/addGrade")
                        .with(user("lecturer1").roles(Role.LECTURER.name()))
                        .param("courseId", aseWS2016.getId().toString())
                        .param("studentId", student.getId().toString())
                        .param("authCode", totp.now())
                        .param("mark", "4")
                        .with(csrf())
        );

        // the grade should exist for the student
        Grade actualGradeStudent = gradeRepository.findAllOfStudent(student).get(0);
        assertEquals(lecturer1, actualGradeStudent.getLecturer());
        assertEquals(student, actualGradeStudent.getStudent());
        assertEquals(aseWS2016, actualGradeStudent.getCourse());
        assertEquals(4, actualGradeStudent.getMark().getMark());

        // and for the lecturer as well
        Grade actualGradeLecturer = gradeRepository.findByLecturerIdAndCourseId(lecturer1.getId(), aseWS2016.getId()).get(0);
        assertEquals(lecturer1, actualGradeLecturer.getLecturer());
        assertEquals(student, actualGradeLecturer.getStudent());
        assertEquals(aseWS2016, actualGradeLecturer.getCourse());
        assertEquals(4, actualGradeLecturer.getMark().getMark());
    }

    @Test
    public void issueGradeFailureWrongAuthCodeTest() throws Exception {

        // given "student" and "lecturer1" with the course "ase"

        // when "lecturer1" issues a grade for "student" with a wrong authentication code
        mockMvc.perform(
                post("/lecturer/course-details/addGrade")
                        .with(user("lecturer1").roles(Role.LECTURER.name()))
                        .param("courseId", aseWS2016.getId().toString())
                        .param("studentId", student.getId().toString())
                        .param("authCode", "-12345")
                        .param("mark", "4")
                        .with(csrf())
        );

        // the grade should not exist
        assertTrue(gradeRepository.findAllOfStudent(student).isEmpty());
        assertTrue(gradeRepository.findByLecturerIdAndCourseId(lecturer1.getId(), aseWS2016.getId()).isEmpty());
    }

}
