package at.ac.tuwien.inso.service.course_recommendation.impl;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.service.course_recommendation.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TagFrequencyScorer tagFrequencyScorer;

    @Override
    public List<Course> recommendCourses(Student student) {
        List<Course> coursesByStudent = courseRepository.findAllForStudent(student);
        List<Course> coursesBySemester = courseRepository.findAllByCurrentSemester();

        List<Course> courses = coursesBySemester.stream()
                .filter(course -> !coursesByStudent.contains(course))
                .collect(Collectors.toList());

        Map<Course, Double> tagFrequencyCourses = tagFrequencyScorer.score(courses, student);

        List<Course> recommendedCourses = new ArrayList<>();

        tagFrequencyCourses.entrySet().stream()
                .sorted(Map.Entry.<Course, Double>comparingByValue()
                        .reversed()).forEachOrdered(it -> recommendedCourses.add(it.getKey()));

        return recommendedCourses;
    }
}
