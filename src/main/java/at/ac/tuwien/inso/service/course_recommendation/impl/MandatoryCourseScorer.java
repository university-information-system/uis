package at.ac.tuwien.inso.service.course_recommendation.impl;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.service.course_recommendation.CourseScorer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class MandatoryCourseScorer implements CourseScorer {

    private static final double MANDATORY = 3;

    @Override
    public Map<Course, Double> score(List<Course> courses, Student student) {
        Map<Course, Double> scoredCourses = courses.stream().collect(toMap(identity(), it -> 0.0));

        courses.forEach(course ->
                student.getStudyplans().forEach(studyplan ->
                        studyplan.getStudyplan().getSubjects().stream()
                                .filter(subjectForStudyPlan ->
                                        subjectForStudyPlan.getSubject().equals(course.getSubject()))
                                .filter(subjectForStudyPlan ->
                                        subjectForStudyPlan.getMandatory())
                                .forEach(subjectForStudyPlan ->
                                        scoredCourses.put(course, scoredCourses.get(course) + MANDATORY))
                )
        );

        return scoredCourses;
    }
}
