package at.ac.tuwien.inso.service.course_recommendation.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.FeedbackRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.TagRepository;
import at.ac.tuwien.inso.service.course_recommendation.TagFrequencyCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TagFrequencyCalculatorImpl implements TagFrequencyCalculator {

    private static final Map<Mark, Double> gradeWeights = new HashMap<Mark, Double>() {
        {
            put(Mark.EXCELLENT, 0.5);
            put(Mark.GOOD, 0.3);
            put(Mark.SATISFACTORY, 0.1);
            put(Mark.SUFFICIENT, 0.1);
            put(Mark.FAILED, -0.5);
        }
    };

    private static final Map<Feedback.Type, Double> feedbackWeights = new HashMap<Feedback.Type, Double>() {
        {
            put(Feedback.Type.LIKE, 1.0);
            put(Feedback.Type.DISLIKE, -1.0);
        }
    };

    @Autowired
    TagRepository tagRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Override
    public Map<Tag, Double> calculate(Student student) {
        List<Course> courses = courseRepository.findAllForStudent(student);
        List<Grade> grades = gradeRepository.findAllOfStudent(student);
        List<Feedback> feedbacks = feedbackRepository.findAllOfStudent(student);

        Map<Tag, Double> tagFrequencies = calculateTagFrequency(courses);
        Map<Tag, Double> tagFrequenciesWithGrades = calculateTagFrequencyWithGrades(courses, grades);
        Map<Tag, Double> tagFrequenciesWithFeedback = calculateTagFrequencyWithFeedback(courses, feedbacks);

        return mergeTagFrequency(mergeTagFrequency(tagFrequencies, tagFrequenciesWithGrades), tagFrequenciesWithFeedback);
    }

    private Map<Tag, Double> calculateTagFrequency(List<Course> courses) {
        Map<Tag, Double> tags = new HashMap<>();
        courses.forEach(course ->
                course.getTags().forEach(it -> tags.put(it, tags.getOrDefault(it, 0.0) + 1))
        );

        return tags;
    }

    private Map<Tag, Double> calculateTagFrequencyWithGrades(List<Course> courses, List<Grade> grades) {
        Map<Tag, Double> tagsWithGrades = new HashMap<>();
        courses.forEach(course ->
                course.getTags().forEach(tag -> {
                            double score = grades.stream()
                                    .filter(grade -> grade.getCourse().equals(course))
                                    .map(grade -> gradeWeights.getOrDefault(grade.getMark(), 0.0))
                                    .findAny()
                                    .orElse(0.0);

                            tagsWithGrades.put(tag, tagsWithGrades.getOrDefault(tag, 0.0) + score);
                        }
                )
        );

        return tagsWithGrades;
    }

    private Map<Tag, Double> calculateTagFrequencyWithFeedback(List<Course> courses, List<Feedback> feedbacks) {
        Map<Tag, Double> tagsWithFeedback = new HashMap<>();

        courses.forEach(course ->
                course.getTags().forEach(tag -> {
                            double score = feedbacks.stream()
                                    .filter(feedback -> feedback.getCourse().equals(course))
                                    .map(feedback -> feedbackWeights.getOrDefault(feedback.getType(), 0.0))
                                    .findAny()
                                    .orElse(0.0);

                            tagsWithFeedback.put(tag, tagsWithFeedback.getOrDefault(tag, 0.0) + score);
                        }
                )
        );

        return tagsWithFeedback;
    }

    private Map<Tag, Double> mergeTagFrequency(Map<Tag, Double> map1, Map<Tag, Double> map2) {
        return Stream
                .concat(map1.entrySet().stream(), map2.entrySet().stream())
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue(),
                        Double::sum
                        )
                );
    }
}
