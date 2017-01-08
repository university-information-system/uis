package at.ac.tuwien.inso.service_tests;

import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.study_progress.*;
import at.ac.tuwien.inso.service.study_progress.impl.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;
import org.springframework.data.util.*;

import java.util.ArrayList;
import java.util.*;
import java.util.stream.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.Comparator.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudyProgressServiceTests {

    @Mock
    private SemesterService semesterService;
    @Mock
    private CourseService courseService;
    @Mock
    private FeedbackService feedbackService;
    @Mock
    private GradeService gradeService;

    @InjectMocks
    private StudyProgressService studyProgressService = new StudyProgressServiceImpl();

    private List<SemesterDto> dtoSemesters;
    private SemesterDto pastSemester;
    private SemesterDto currentSemester;

    private Student student = new Student("123", "student", "mail@uis.at");

    @Before
    public void setUp() throws Exception {
    	dtoSemesters = new ArrayList<>();
        LongStream.range(1, 5).forEach(it -> {
            SemesterDto semester = new SemesterDto("label"+it);
            //when(semester.getId()).thenReturn(it);
            semester.setId(it);

            dtoSemesters.add(semester);
        });
        pastSemester = dtoSemesters.get(dtoSemesters.size() - 2);
        currentSemester = dtoSemesters.get(dtoSemesters.size() - 1);

        when(semesterService.getCurrentSemester()).thenReturn(currentSemester);
    }

    @Test
    public void itSetsCurrentSemesterForStudyProgress() throws Exception {
        StudyProgress studyProgress = studyProgressService.studyProgressFor(student);

        assertEquals(currentSemester, studyProgress.getCurrentSemester());
    }

    @Test
    public void studyProgressForStudentWithEmptyStuyPlans() throws Exception {
        StudyProgress studyProgress = studyProgressService.studyProgressFor(student);

        assertEquals(emptyList(), studyProgress.getSemestersProgress());
    }

    @Test
    public void studyProgressForStudentWithEmptyCourseRegistrations() throws Exception {
        prepareStudyPlanRegistrationsFor(dtoSemesters.get(2), dtoSemesters.get(1));

        checkStudentHasSemestersProgress(
                new SemesterProgress(dtoSemesters.get(3), emptyList()),
                new SemesterProgress(dtoSemesters.get(2), emptyList()),
                new SemesterProgress(dtoSemesters.get(1), emptyList())
        );
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private List<StudyPlanRegistration> prepareStudyPlanRegistrationsFor(SemesterDto... semesters) {
        
    	Stream.of(semesters)
                .map(it -> new StudyPlanRegistration(mock(StudyPlan.class), it.toEntity()))
                .forEach(it2 -> student.addStudyplans(it2));
    	
        SemesterDto firstSemester = Stream.of(semesters).min(comparing(SemesterDto::getId)).get();
        List<SemesterDto> studentSemesters = this.dtoSemesters.stream()
                .filter(it -> it.getId() >= firstSemester.getId())
                .collect(Collectors.toList());
        Collections.reverse(studentSemesters);
        List<SemesterDto> studentSemestersDtos = new ArrayList<SemesterDto>();
        for(SemesterDto s : studentSemesters){
        	studentSemestersDtos.add(s);
        }
        
        when(semesterService.findAllSince(firstSemester)).thenReturn(studentSemestersDtos);


        return student.getStudyplans();
    }

    private void checkStudentHasSemestersProgress(SemesterProgress... semestersProgress) {
        StudyProgress studyProgress = studyProgressService.studyProgressFor(student);
        
        assertEquals(asList(semestersProgress), studyProgress.getSemestersProgress());
    }

    @Test
    public void studyProgressForStudentWithOpenCourses() throws Exception {
        prepareStudyPlanRegistrationsFor(pastSemester);
        List<Course> courses = prepareCoursesFor(pastSemester, currentSemester);

        checkStudentHasSemestersProgress(
                new SemesterProgress(currentSemester, singletonList(new CourseRegistration(courses.get(1)))),
                new SemesterProgress(pastSemester, singletonList(new CourseRegistration(courses.get(0), CourseRegistrationState.needs_feedback)))
        );
    }

    private List<Course> prepareCoursesFor(SemesterDto... semesters) {
        List<Course> courses = Stream.of(semesters)
                .map(it -> new Course(mock(Subject.class), it.toEntity()))
                .collect(Collectors.toList());

        when(courseService.findAllForStudent(student)).thenReturn(courses);

        return courses;
    }

    @Test
    public void studyProgressForStudentWithGradedCourses() throws Exception {
        prepareStudyPlanRegistrationsFor(pastSemester);
        List<Course> courses = prepareCoursesFor(pastSemester, currentSemester);
        prepareGradesFor(courses.stream().map(it -> Pair.of(it, Mark.EXCELLENT)).collect(Collectors.toList()));

        checkStudentHasSemestersProgress(
                new SemesterProgress(currentSemester, singletonList(new CourseRegistration(courses.get(1), CourseRegistrationState.needs_feedback))),
                new SemesterProgress(pastSemester, singletonList(new CourseRegistration(courses.get(0), CourseRegistrationState.needs_feedback)))
        );
    }

    private List<Grade> prepareGradesFor(List<Pair<Course, Mark>> coursesWithMarks) {
        List<Grade> grades = coursesWithMarks.stream()
                .map(it -> new Grade(it.getFirst(), mock(Lecturer.class), student, it.getSecond()))
                .collect(Collectors.toList());

        when(gradeService.findAllOfStudent(student)).thenReturn(grades);

        return grades;
    }

    @Test
    public void studyProgressForStudentWithFeedbackForCourses() throws Exception {
        prepareStudyPlanRegistrationsFor(pastSemester);
        List<Course> courses = prepareCoursesFor(pastSemester, currentSemester);
        prepareFeedbackFor(courses.get(0), courses.get(1));

        checkStudentHasSemestersProgress(
                new SemesterProgress(currentSemester, singletonList(new CourseRegistration(courses.get(1), CourseRegistrationState.needs_grade))),
                new SemesterProgress(pastSemester, singletonList(new CourseRegistration(courses.get(0), CourseRegistrationState.needs_grade)))
        );
    }

    private List<Feedback> prepareFeedbackFor(Course... courses) {
        List<Feedback> feedback = Stream.of(courses)
                .map(it -> new Feedback(student, it))
                .collect(Collectors.toList());

        when(feedbackService.findAllOfStudent(student)).thenReturn(feedback);

        return feedback;
    }

    @Test
    public void studyProgressForStudentWithGradesAndFeedbackForCourses() throws Exception {
        prepareStudyPlanRegistrationsFor(pastSemester);
        List<Course> courses = prepareCoursesFor(pastSemester, currentSemester);
        prepareGradesFor(asList(
                Pair.of(courses.get(0), Mark.EXCELLENT),
                Pair.of(courses.get(1), Mark.FAILED)
        ));
        prepareFeedbackFor(courses.get(0), courses.get(1));

        checkStudentHasSemestersProgress(
                new SemesterProgress(currentSemester, singletonList(new CourseRegistration(courses.get(1), CourseRegistrationState.complete_not_ok))),
                new SemesterProgress(pastSemester, singletonList(new CourseRegistration(courses.get(0), CourseRegistrationState.complete_ok)))
        );
    }
}
