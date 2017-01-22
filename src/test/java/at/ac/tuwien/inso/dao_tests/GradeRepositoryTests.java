package at.ac.tuwien.inso.dao_tests;

import static at.ac.tuwien.inso.utils.IterableUtils.toList;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Mark;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GradeRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;

    private List<Student> students;

    @Before
    public void setUp() throws Exception {
        students = toList(studentRepository.save(asList(
                new Student("123", "student", "student@uis.at"),
                new Student("456", "student", "student@uis.at")
        )));
    }

    @Test
    public void findAllOfStudentWithNoGrades() throws Exception {
        prepareGradeFor(students.get(0));

        List<Grade> grades = gradeRepository.findAllOfStudent(students.get(1));

        assertThat(grades, empty());
    }

    private Grade prepareGradeFor(Student student) {
        Subject subject = subjectRepository.save(new Subject("subject", BigDecimal.ONE));
        Semester semester = semesterRepository.save(new Semester(2016, SemesterType.SummerSemester));
        Course course = courseRepository.save(new Course(subject, semester));
        Lecturer lecturer = lecturerRepository.save(new Lecturer("abc", "lecturer", "lecturer@uis.at"));

        return gradeRepository.save(new Grade(course, lecturer, student, Mark.EXCELLENT));
    }

    @Test
    public void findAllOfStudentWithSomeFeedbackEntries() throws Exception {
        Grade grade = prepareGradeFor(students.get(0));

        List<Grade> grades = gradeRepository.findAllOfStudent(students.get(0));

        assertThat(grades, equalTo(singletonList(grade)));
    }
}
