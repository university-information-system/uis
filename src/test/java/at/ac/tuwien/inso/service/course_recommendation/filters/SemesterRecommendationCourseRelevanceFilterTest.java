package at.ac.tuwien.inso.service.course_recommendation.filters;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;

@RunWith(MockitoJUnitRunner.class)
public class SemesterRecommendationCourseRelevanceFilterTest {

    private final Student student = mock(Student.class);
    private final StudyPlan studyPlan = mock(StudyPlan.class);
    private final Semester registrationSemester = mock(Semester.class);
    private final Subject subjectWithoutSemester = mock(Subject.class);
    private final Course courseWithoutSemester = new Course(subjectWithoutSemester);
    private final Subject subjectForSemester3 = mock(Subject.class);
    private final Course courseForSemester3 = new Course(subjectForSemester3);
    private final List<Course> allCourses = asList(courseWithoutSemester, courseForSemester3);
    @Mock
    private SemesterRepository semesterRepository;
    @Mock
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;
    @InjectMocks
    private SemesterRecommendationCourseRelevanceFilter filter;

    @Before
    public void setUp() throws Exception {
        when(student.getStudyplans()).thenReturn(singletonList(new StudyPlanRegistration(studyPlan, registrationSemester)));

        when(subjectForStudyPlanRepository
                .findBySubjectInAndStudyPlan(asList(subjectWithoutSemester, subjectForSemester3), studyPlan))
                .thenReturn(asList(
                        new SubjectForStudyPlan(subjectWithoutSemester, studyPlan, true, null),
                        new SubjectForStudyPlan(subjectForSemester3, studyPlan, true, 3)
                ));
    }

    @Test
    public void itKeepsCourseWithoutSemesterRecommendation() throws Exception {
        givenStudentIsInSemester(1);

        List<Course> courses = filter.filter(allCourses, student);

        assertThat(courses, equalTo(singletonList(courseWithoutSemester)));
    }

    @Test
    public void itKeepsCourseWithMatchingSemesterRecommendation() throws Exception {
        givenStudentIsInSemester(3);

        List<Course> courses = filter.filter(allCourses, student);

        assertThat(courses, equalTo(allCourses));
    }

    private void givenStudentIsInSemester(int semesterNo) {
        List<Semester> semesters = IntStream.range(0, semesterNo).mapToObj(it -> mock(Semester.class)).collect(Collectors.toList());
        when(semesterRepository.findAllSince(registrationSemester)).thenReturn(semesters);
    }
}