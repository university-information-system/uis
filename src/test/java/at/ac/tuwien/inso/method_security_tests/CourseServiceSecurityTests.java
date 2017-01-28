package at.ac.tuwien.inso.method_security_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.controller.lecturer.forms.AddCourseForm;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.repository.UisUserRepository;
import at.ac.tuwien.inso.service.CourseService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CourseServiceSecurityTests {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private LecturerRepository lecturerRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    private Lecturer lecturer;
    private Course course;
    private Student student;
    private Student adminaccount;
    private UserAccount user = new UserAccount("student1", "pass", Role.STUDENT);
    private UserAccount admin = new UserAccount("admin1", "pass", Role.ADMIN);
    private UserAccount lecturerRole = new UserAccount("Lecturer", "pass", Role.LECTURER);

    @BeforeTransaction
    public void beforeTransaction() {
        student = studentRepository.save(new Student("1", "student1", "student@student.com", user));
        //workaround because it is not possible to instanceise an admin
        adminaccount = studentRepository.save(new Student("2", "admin1", "admin@test.com", admin));
    }

    @AfterTransaction
    public void afterTransaction() {
        studentRepository.delete(student);
        studentRepository.delete(adminaccount);
    }

    @Before
    public void setUp() {
        lecturer = lecturerRepository.save(new Lecturer("2", "Lecturer", "lecturer@lecturer.com", lecturerRole));
        Subject subject = subjectRepository.save(new Subject("ASE", BigDecimal.valueOf(6)));
        subject.addLecturers(lecturer);
        subjectRepository.save(subject);
        Semester semester = semesterRepository.save(new Semester(2016, SemesterType.WinterSemester));
        course = courseRepository.save(new Course(subject, semester));
        course.setStudentLimits(0);
        course.addStudents(student);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findCourseForCurrentSemesterWithNameNotAuthenticated() {
        courseService.findCourseForCurrentSemesterWithName("test", new PageRequest(1, 1));
    }

    @Test
    @WithMockUser
    public void findCourseForCurrentSemesterWithNameAuthenticated() {
        Page<Course> results = courseService.findCourseForCurrentSemesterWithName("ASE", new PageRequest(0, 1));
        assertFalse(results.getContent().isEmpty());
        assertEquals(results.getContent().get(0).getSubject().getName(), "ASE");
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findCoursesForCurrentSemesterForLecturerNotAuthenticated() {
        courseService.findCoursesForCurrentSemesterForLecturer(lecturer);
    }

    @Test
    @WithMockUser
    public void findCoursesForCurrentSemesterForLecturerAuthenticated() {
        List<Course> courses = courseService.findCoursesForCurrentSemesterForLecturer(lecturer);
        assertTrue(courses.size() == 1);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void saveCourseNotAuthenticated() {
        AddCourseForm addCourseForm = new AddCourseForm(course);
        courseService.saveCourse(addCourseForm);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "student1", roles = "STUDENT")
    public void saveCourseAuthenticatedAsStudent() {
        AddCourseForm addCourseForm = new AddCourseForm(course);
        courseService.saveCourse(addCourseForm);
    }

    @Test
    @WithMockUser(roles = "ADMIN", username="admin1")
    public void saveCourseAuthenticatedAsAdmin() {
        AddCourseForm addCourseForm = new AddCourseForm(course);
        Course result = courseService.saveCourse(addCourseForm);
        assertTrue(addCourseForm.getCourse().equals(result));
    }

    @Test
    @WithMockUser(roles = "LECTURER", username="Lecturer")
    public void saveCourseAuthenticatedAsLecturer() {
        AddCourseForm addCourseForm = new AddCourseForm(course);
        Course result = courseService.saveCourse(addCourseForm);
        assertTrue(addCourseForm.getCourse().equals(result));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findOneNotAuthenticated() {
        courseService.findOne(Long.parseLong("1"));
    }

    @Test
    @WithMockUser
    public void findOneAuthenticated() {
        Course result = courseService.findOne(course.getId());
        assertTrue(course.getId().equals(result.getId()));
    }
    

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void registerStudentForCourseNotAuthenticated() {
        courseService.registerStudentForCourse(course);
    }
    
    @Test(expected = ValidationException.class)
    @WithMockUser(roles = "LECTURER", username="Lecturer")
    public void removeCourseAuthenticatedButHasStudents(){
    	AddCourseForm addCourseForm = new AddCourseForm(course);
        Course result = courseService.saveCourse(addCourseForm);
        assertTrue(addCourseForm.getCourse().equals(result));
    	
        courseService.remove(result.getId());
        Course result2 = courseService.findOne(course.getId());
    }
    
    @Test(expected = BusinessObjectNotFoundException.class)
    @WithMockUser(roles = "LECTURER", username="Lecturer")
    public void removeCourseAuthenticated(){
    	course.removeStudents(student);
    	courseRepository.save(course);
    	AddCourseForm addCourseForm = new AddCourseForm(course);
        Course result = courseService.saveCourse(addCourseForm);
        assertTrue(addCourseForm.getCourse().equals(result));
    	
        courseService.remove(result.getId());
        Course result2 = courseService.findOne(course.getId());
    }
    
    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void removeCourseNotAuthenticated(){
    	courseService.remove(course.getId());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ADMIN")
    public void registerStudentForCourseAuthenticatedAsAdmin() {
        courseService.registerStudentForCourse(course);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void registerStudentForCourseAuthenticatedAsLecturer() {
        courseService.registerStudentForCourse(course);
    }

    @Test
    @WithUserDetails(value = "student1")
    public void registerStudentForCourseAuthenticatedAsStudent() {
        assertFalse(courseService.registerStudentForCourse(course));
    }

}
