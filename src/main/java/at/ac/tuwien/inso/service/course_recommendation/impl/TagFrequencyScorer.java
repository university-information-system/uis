package at.ac.tuwien.inso.service.course_recommendation.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.course_recommendation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class TagFrequencyScorer implements CourseScorer {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TagFrequencyCalculatorImpl tagFrequencyCalculator;

    @Value("${uis.course.recommender.tag.scorer.weight:1}")
    private Double weight;

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public Map<Course, Double> score(List<Course> courses, Student student) {
        Map<Tag, Double> tagFrequencies = tagFrequencyCalculator.calculate(student);
        Map<Course, Double> scoredCourses = new HashMap<>();

        courses.forEach(course -> {
                    double score = course.getTags().stream()
                            .filter(tag -> tagFrequencies.containsKey(tag))
                            .mapToDouble(tag -> tagFrequencies.get(tag)).sum();

                    scoredCourses.put(course, score);
                }
        );

        return scoredCourses;
    }
}
