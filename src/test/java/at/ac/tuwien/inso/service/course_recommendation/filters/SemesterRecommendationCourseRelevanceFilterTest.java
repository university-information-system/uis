package at.ac.tuwien.inso.service.course_recommendation.filters;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import java.util.*;
import java.util.stream.*;

import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SemesterRecommendationCourseRelevanceFilterTest {

    private final Student student = mock(Student.class);
    private final StudyPlan studyPlan = mock(StudyPlan.class);
    private final Semester registrationSemester = mock(Semester.class);
    private final Subject subjectWithoutSemester = mock(Subject.class);
    private final Course courseWithoutSemester = new Course(subjectWithoutSemester, null);
    private final Subject subjectForSemester3 = mock(Subject.class);
    private final Course courseForSemester3 = new Course(subjectForSemester3, null);
    private final List<Course> allCourses = Arrays.asList(courseWithoutSemester, courseForSemester3);
    @Mock
    private SemesterRepository semesterRepository;
    @Mock
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;
    @InjectMocks
    private SemesterRecommendationCourseRelevanceFilter filter;

    @Before
    public void setUp() throws Exception {
        when(student.getStudyplans()).thenReturn(singletonList(new StudyPlanRegistration(studyPlan, registrationSemester)));

        when(subjectForStudyPlanRepository.findBySubjectAndStudyPlan(subjectWithoutSemester, studyPlan))
                .thenReturn(new SubjectForStudyPlan(subjectWithoutSemester, studyPlan, true, null));
        when(subjectForStudyPlanRepository.findBySubjectAndStudyPlan(subjectForSemester3, studyPlan))
                .thenReturn(new SubjectForStudyPlan(subjectForSemester3, studyPlan, true, 3));
    }

    @Test
    public void itKeepsCourseWithoutSemesterRecommendation() throws Exception {
        givenStudentIsInSemester(1);

        List<Course> courses = filter.filter(allCourses.stream(), student).collect(Collectors.toList());

        assertThat(courses, equalTo(singletonList(courseWithoutSemester)));
    }

    @Test
    public void itKeepsCourseWithMatchingSemesterRecommendation() throws Exception {
        givenStudentIsInSemester(3);

        List<Course> courses = filter.filter(allCourses.stream(), student).collect(Collectors.toList());

        assertThat(courses, equalTo(allCourses));
    }

    private void givenStudentIsInSemester(int semesterNo) {
        List<Semester> semesters = IntStream.range(0, semesterNo).mapToObj(it -> mock(Semester.class)).collect(Collectors.toList());
        when(semesterRepository.findAllSince(registrationSemester)).thenReturn(semesters);
    }
}