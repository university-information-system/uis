package at.ac.tuwien.inso.service.course_recommendation.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.course_recommendation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final Long N_MAX_COURSE_RECOMMENDATIONS = 5L;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TagFrequencyScorer tagFrequencyScorer;

    @Override
    public List<Course> recommendCourses(Student student) {
        List<Course> courses = courseRepository.findAllRecommendableForStudent(student);

        Map<Course, Double> tagFrequencyCourses = tagFrequencyScorer.score(courses, student);

        List<Course> recommendedCourses = new ArrayList<>();

        tagFrequencyCourses.entrySet().stream()
                .sorted(Map.Entry.<Course, Double>comparingByValue()
                        .reversed()).forEachOrdered(it -> recommendedCourses.add(it.getKey()));

        return recommendedCourses.stream().limit(N_MAX_COURSE_RECOMMENDATIONS).collect(Collectors.toList());
    }
}
