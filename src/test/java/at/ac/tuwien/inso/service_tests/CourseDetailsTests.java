package at.ac.tuwien.inso.service_tests;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import at.ac.tuwien.inso.dto.CourseDetailsForStudent;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.SemesterService;
import at.ac.tuwien.inso.service.impl.CourseServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class CourseDetailsTests {

    private Semester olderSemester = new Semester(2015, SemesterType.WinterSemester);
    private Semester currentSemester = new Semester(2016, SemesterType.WinterSemester);

    private Subject subject = new Subject("subject", BigDecimal.ONE);

    private Student student = new Student("1", "student", "student@uis.at");

    private Long courseId = 1L;
    private Course course = new Course(subject, currentSemester, "some description");

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private SemesterService semesterService;
    @Mock
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;
    @InjectMocks
    private CourseService courseService = new CourseServiceImpl();

    @Before
    public void setUp() throws Exception {

        when(semesterService.getCurrentSemester()).thenReturn(currentSemester.toDto());
        when(semesterService.getOrCreateCurrentSemester()).thenReturn(currentSemester.toDto());

        when(subjectForStudyPlanRepository.findBySubject(subject)).thenReturn(emptyList());

        when(courseRepository.findOne(courseId)).thenReturn(course);
    }

    @Test(expected = BusinessObjectNotFoundException.class)
    public void itThrowsOnUnknownCourse() throws Exception {
        courseService.courseDetailsFor(student, 2L);
    }

    @Test
    public void testCanEnrollForCourseInOlderSemester() throws Exception {
        when(courseRepository.findOne(courseId)).thenReturn(new Course(subject, olderSemester));

        CourseDetailsForStudent details = courseService.courseDetailsFor(student, courseId);

        assertFalse(details.getCanEnroll());
    }

    @Test
    public void testCanEnrollForCourseAlreadyRegisteredFor() throws Exception {
        when(courseRepository.existsCourseRegistration(student, course)).thenReturn(true);

        CourseDetailsForStudent details = courseService.courseDetailsFor(student, courseId);

        assertFalse(details.getCanEnroll());
    }

    @Test
    public void testCanEnrollForCourseInCurrentSemesterNotRegisteredFor() throws Exception {
        when(courseRepository.existsCourseRegistration(student, course)).thenReturn(false);

        CourseDetailsForStudent details = courseService.courseDetailsFor(student, courseId);

        assertTrue(details.getCanEnroll());
    }

    @Test
    public void testGeneralDetails() throws Exception {
        CourseDetailsForStudent details = courseService.courseDetailsFor(student, courseId);

        assertEquals(subject.getName(), details.getName());
        assertEquals(subject.getEcts(), details.getEcts());
        assertEquals(currentSemester.getLabel(), details.getSemester());
        assertEquals(course.getDescription(), details.getDescription());
    }

    @Test
    public void testTags() throws Exception {
        course.addTags(new Tag("tag 1"), new Tag("tag 2"));
        List<String> tags = course.getTags().stream().map(Tag::getName).collect(toList());

        CourseDetailsForStudent details = courseService.courseDetailsFor(student, courseId);

        assertEquals(tags, details.getTags());
    }

    @Test
    public void testLecturers() throws Exception {
        subject.addLecturers(new Lecturer("2", "lecturer", "lecturer@uis.at"));

        CourseDetailsForStudent details = courseService.courseDetailsFor(student, courseId);

        assertEquals(subject.getLecturers(), details.getLecturers());
    }

    @Test
    public void testStudyPlans() throws Exception {
        List<SubjectForStudyPlan> studyplans = singletonList(mock(SubjectForStudyPlan.class));
        when(subjectForStudyPlanRepository.findBySubject(subject)).thenReturn(studyplans);

        CourseDetailsForStudent details = courseService.courseDetailsFor(student, courseId);

        assertEquals(studyplans, details.getStudyplans());
    }

}
