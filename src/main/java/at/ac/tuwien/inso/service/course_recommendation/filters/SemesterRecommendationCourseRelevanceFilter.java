package at.ac.tuwien.inso.service.course_recommendation.filters;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

@Component
public class SemesterRecommendationCourseRelevanceFilter implements CourseRelevanceFilter {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    @Override
    public List<Course> filter(List<Course> courses, Student student) {
        Map<StudyPlan, Integer> studentSemesters = student.getStudyplans().stream()
                .collect(toMap(
                        StudyPlanRegistration::getStudyplan,
                        it -> semesterRepository.findAllSince(it.getRegisteredSince()).size())
                );

        List<Subject> subjects = courses.stream().map(Course::getSubject).collect(Collectors.toList());
        Map<StudyPlan, Map<Subject, Integer>> courseSemesterRecommendations = student.getStudyplans().stream()
                .map(StudyPlanRegistration::getStudyplan)
                .collect(toMap(
                        identity(),
                        it -> {
                            Map<Subject, Integer> subjectToSemesterRecommendation = new HashMap<>();
                            subjectForStudyPlanRepository.findBySubjectInAndStudyPlan(subjects, it).forEach(subjectForStudyPlan -> {
                                subjectToSemesterRecommendation.put(subjectForStudyPlan.getSubject(), subjectForStudyPlan.getSemesterRecommendation());
                            });
                            return subjectToSemesterRecommendation;
                        }
                ));

        return courses.stream().filter(course ->
                studentSemesters.keySet().stream().anyMatch(studyPlan -> {
                    Integer studentSemester = studentSemesters.get(studyPlan);
                    Integer courseSemester = courseSemesterRecommendations.get(studyPlan).get(course.getSubject());

                    return courseSemester == null ||
                            studentSemester >= courseSemester;
                })
        ).collect(Collectors.toList());
    }
}
