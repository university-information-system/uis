package at.ac.tuwien.inso.service.course_recommendation.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.course_recommendation.*;
import at.ac.tuwien.inso.service.course_recommendation.filters.*;
import at.ac.tuwien.inso.service.course_recommendation.normalization.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

import static java.util.function.Function.*;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final Long N_MAX_COURSE_RECOMMENDATIONS = 5L;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseNormalizer courseNormalizer;

    private List<CourseRelevanceFilter> courseRelevanceFilters;

    private List<CourseScorer> courseScorers;
    private double courseScorersWeights;

    @Autowired
    public RecommendationServiceImpl setCourseRelevanceFilters(List<CourseRelevanceFilter> courseRelevanceFilters) {
        this.courseRelevanceFilters = courseRelevanceFilters;
        return this;
    }

    @Autowired
    public RecommendationServiceImpl setCourseScorers(List<CourseScorer> courseScorers) {
        this.courseScorers = courseScorers;
        courseScorersWeights = courseScorers.stream().mapToDouble(CourseScorer::weight).sum();
        return this;
    }

    @Override
    public List<Course> recommendCourses(Student student) {
        List<Course> courses = getRecommendableCoursesFor(student);

        // Compute initial scores
        Map<CourseScorer, Map<Course, Double>> scores = courseScorers.stream()
                .collect(Collectors.toMap(
                        identity(),
                        it -> it.score(courses, student)
                ));

        // Normalize scores
        scores.values().forEach(it -> courseNormalizer.normalize(it));

        // Aggregate scores, by scorer weights
        Map<Course, Double> recommendedCourseMap = courses.stream()
                .collect(Collectors.toMap(
                        identity(),
                        course -> {
                            double aggregatedScore = scores.keySet().stream()
                                    .mapToDouble(scorer -> scores.get(scorer).get(course) * scorer.weight())
                                    .sum();
                            return aggregatedScore / courseScorersWeights;
                        }
                ));

        // Sort courses by score
        return recommendedCourseMap.entrySet().stream()
                .sorted(Map.Entry.<Course, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<Course, Double> mergeMaps(Map<Course, Double> map1, Map<Course, Double> map2) {
        return Stream
                .concat(map1.entrySet().stream(), map2.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Double::sum
                        )
                );
    }

    private List<Course> getRecommendableCoursesFor(Student student) {
        List<Course> courses = courseRepository.findAllRecommendableForStudent(student);

        for (CourseRelevanceFilter filter : courseRelevanceFilters) {
            courses = filter.filter(courses, student);
        }

        return courses;
    }
}
