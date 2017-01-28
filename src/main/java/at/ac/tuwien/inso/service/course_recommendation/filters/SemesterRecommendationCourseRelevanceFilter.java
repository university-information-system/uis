package at.ac.tuwien.inso.service.course_recommendation.filters;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;

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
