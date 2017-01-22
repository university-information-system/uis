package at.ac.tuwien.inso.service.course_recommendation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.service.course_recommendation.CourseScorer;

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
