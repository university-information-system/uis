package at.ac.tuwien.inso.service_tests.course_recommendation;

import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;
import at.ac.tuwien.inso.service.course_recommendation.impl.MandatoryCourseScorer;

@RunWith(MockitoJUnitRunner.class)
public class MandatoryCourseScorerTest {

    @InjectMocks
    private MandatoryCourseScorer mandatoryCourseScorer;

    @Mock
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    private Student student = mock(Student.class);

    private List<Subject> subjects = asList(
            mock(Subject.class),
            mock(Subject.class),
            mock(Subject.class)
    );

    private List<Course> courses = asList(
            new Course(subjects.get(0), null),
            new Course(subjects.get(1), null),
            new Course(subjects.get(2), null)
    );

    private List<StudyPlan> studyPlans = asList(
            mock(StudyPlan.class),
            mock(StudyPlan.class),
            mock(StudyPlan.class)
    );

    private List<SubjectForStudyPlan> subjectsForStudyPlan = asList(
            new SubjectForStudyPlan(subjects.get(0), studyPlans.get(0), false),
            new SubjectForStudyPlan(subjects.get(0), studyPlans.get(1), true),
            new SubjectForStudyPlan(subjects.get(1), studyPlans.get(1), false),
            new SubjectForStudyPlan(subjects.get(0), studyPlans.get(2), true),
            new SubjectForStudyPlan(subjects.get(1), studyPlans.get(2), true)
    );

    private List<StudyPlanRegistration> studyPlanRegistrations = asList(
            mock(StudyPlanRegistration.class),
            mock(StudyPlanRegistration.class),
            mock(StudyPlanRegistration.class)
    );

    @Test
    public void verifyCourseScorerWithNoMandatorySubjectsAndOneStudyPlan() throws Exception {
        when(student.getStudyplans()).thenReturn(asList(studyPlanRegistrations.get(0)));
        when(studyPlanRegistrations.get(0).getStudyplan()).thenReturn(studyPlans.get(0));
        when(subjectForStudyPlanRepository.findBySubjectInAndStudyPlan(
                subjects,
                studyPlans.get(0))
        ).thenReturn(asList(
                subjectsForStudyPlan.get(0))
        );

        Map<Course, Double> scores = mandatoryCourseScorer.score(courses, student);
        Map<Course, Double> expectedScores = courses.stream().collect(toMap(identity(), it -> 0.0));

        assertEquals(expectedScores, scores);
    }

    @Test
    public void verifyCourseScorerWithMandatorySubjectsAndOneStudyPlan() throws Exception {
        when(student.getStudyplans()).thenReturn(asList(studyPlanRegistrations.get(1)));
        when(studyPlanRegistrations.get(1).getStudyplan()).thenReturn(studyPlans.get(1));
        when(subjectForStudyPlanRepository.findBySubjectInAndStudyPlan(
                subjects,
                studyPlans.get(1))
        ).thenReturn(asList(
                subjectsForStudyPlan.get(1),
                subjectsForStudyPlan.get(2))
        );

        Map<Course, Double> scores = mandatoryCourseScorer.score(courses, student);
        Map<Course, Double> expectedScores = new HashMap<Course, Double>() {
            {
                put(courses.get(0), 3.0);
                put(courses.get(1), 0.0);
                put(courses.get(2), 0.0);
            }
        };

        assertEquals(expectedScores, scores);
    }

    @Test
    public void verifyCourseScorerWithMandatorySubjectsAndMoreStudyPlans() throws Exception {
        when(student.getStudyplans()).thenReturn(asList(
                studyPlanRegistrations.get(1),
                studyPlanRegistrations.get(2))
        );
        when(studyPlanRegistrations.get(1).getStudyplan()).thenReturn(studyPlans.get(1));
        when(studyPlanRegistrations.get(2).getStudyplan()).thenReturn(studyPlans.get(2));
        when(subjectForStudyPlanRepository.findBySubjectInAndStudyPlan(
                subjects,
                studyPlans.get(1))
        ).thenReturn(asList(
                subjectsForStudyPlan.get(1),
                subjectsForStudyPlan.get(2))
        );
        when(subjectForStudyPlanRepository.findBySubjectInAndStudyPlan(
                subjects,
                studyPlans.get(2))
        ).thenReturn(asList(
                subjectsForStudyPlan.get(3),
                subjectsForStudyPlan.get(4))
        );

        Map<Course, Double> scores = mandatoryCourseScorer.score(courses, student);
        Map<Course, Double> expectedScores = new HashMap<Course, Double>() {
            {
                put(courses.get(0), 6.0);
                put(courses.get(1), 3.0);
                put(courses.get(2), 0.0);
            }
        };

        assertEquals(expectedScores, scores);
    }
}