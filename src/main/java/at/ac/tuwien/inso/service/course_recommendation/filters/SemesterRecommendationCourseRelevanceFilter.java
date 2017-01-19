package at.ac.tuwien.inso.service.course_recommendation.filters;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Component
public class SemesterRecommendationCourseRelevanceFilter implements CourseRelevanceFilter {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    @Override
    public Stream<Course> filter(Stream<Course> courses, Student student) {
        Map<StudyPlan, Integer> studentSemesters = student.getStudyplans().stream()
                .collect(Collectors.toMap(
                        StudyPlanRegistration::getStudyplan,
                        it -> semesterRepository.findAllSince(it.getRegisteredSince()).size())
                );

        return courses.filter(course ->
                studentSemesters.keySet().stream().anyMatch(studyPlan -> {
                    Integer studentSemester = studentSemesters.get(studyPlan);

                    SubjectForStudyPlan subjectForStudyPlan = subjectForStudyPlanRepository.findBySubjectAndStudyPlan(course.getSubject(), studyPlan);

                    return subjectForStudyPlan == null ||
                            subjectForStudyPlan.getSemesterRecommendation() == null ||
                            studentSemester >= subjectForStudyPlan.getSemesterRecommendation();
                }));
    }
}
