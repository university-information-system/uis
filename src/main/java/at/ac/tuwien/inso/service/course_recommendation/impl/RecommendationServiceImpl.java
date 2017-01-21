package at.ac.tuwien.inso.service.course_recommendation.impl;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.service.course_recommendation.RecommendationService;
import at.ac.tuwien.inso.service.course_recommendation.filters.CourseRelevanceFilter;
import at.ac.tuwien.inso.service.course_recommendation.normalization.CourseNormalizer;
import at.ac.tuwien.inso.service.course_recommendation.user_based.UserBasedCourseScorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final Long N_MAX_COURSE_RECOMMENDATIONS = 5L;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TagFrequencyScorer tagFrequencyScorer;

    @Autowired
    private MandatoryCourseScorer mandatoryCourseScorer;

    @Autowired
    private CourseNormalizer courseNormalizer;

    @Autowired
    private UserBasedCourseScorer userBasedCourseScorer;

    private List<CourseRelevanceFilter> courseRelevanceFilters;

    @Autowired
    public RecommendationServiceImpl setCourseRelevanceFilters(List<CourseRelevanceFilter> courseRelevanceFilters) {
        this.courseRelevanceFilters = courseRelevanceFilters;
        return this;
    }

    @Override
    public List<Course> recommendCourses(Student student) {
        List<Course> courses = getRecommendableCoursesFor(student);

        Map<Course, Double> mandatoryCourses = mandatoryCourseScorer.score(courses, student);
        Map<Course, Double> tagFrequencyCourses = tagFrequencyScorer.score(courses, student);
        Map<Course, Double> userBasedCourses = userBasedCourseScorer.score(courses, student);

        courseNormalizer.normalize(mandatoryCourses);
        courseNormalizer.normalize(tagFrequencyCourses);
        courseNormalizer.normalize(userBasedCourses);

        Map<Course, Double> recommendedCourseMap = mergeMaps(mandatoryCourses, tagFrequencyCourses);
        recommendedCourseMap = mergeMaps(recommendedCourseMap, userBasedCourses);

        List<Course> recommendedCourses = new ArrayList<>();

        recommendedCourseMap.entrySet().stream()
                .sorted(Map.Entry.<Course, Double>comparingByValue()
                        .reversed()).forEachOrdered(it -> recommendedCourses.add(it.getKey()));

        return recommendedCourses.stream().limit(N_MAX_COURSE_RECOMMENDATIONS).collect(Collectors.toList());
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
