package at.ac.tuwien.inso.integration_tests;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.math.BigDecimal;

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

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Mark;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.FeedbackRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.repository.TagRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LecturerCourseDetailsTests {


    UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    Lecturer lecturer1 = new Lecturer("l0001", "Lecturer 1", "email", user1);
    Lecturer lecturer2 = new Lecturer("l0002", "Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER));
    Lecturer lecturer3 = new Lecturer("l0003", "Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER));
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
        Student st2 = new Student("st2", "Student2", "st2@ude.nt", new UserAccount("st2", "pass", Role.STUDENT));
        Student st3 = new Student("st3", "Student3", "st3@ude.nt", new UserAccount("st3", "pass", Role.STUDENT));
        Student st4 = new Student("st4", "Student4", "st4@ude.nt", new UserAccount("st4", "pass", Role.STUDENT));
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
        Student st1 = studentRepository.save(new Student("stud1", "Student2", "st1@ude.nt", new UserAccount("stud1", "pass", Role.STUDENT)));
        Student st2 = studentRepository.save(new Student("stud2", "Student3", "st2@ude.nt", new UserAccount("stud2", "pass", Role.STUDENT)));
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
    public void lecturerShouldSeeCourseDetailsTest() {
        //TODO
    }

    @Test
    public void issueGradeSuccessTest() {
        //TODO
    }

    @Test
    public void issueGradeFailureTest() {
        //TODO
    }


}
