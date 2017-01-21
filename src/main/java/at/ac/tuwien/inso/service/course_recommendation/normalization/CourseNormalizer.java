package at.ac.tuwien.inso.service.course_recommendation.normalization;

import at.ac.tuwien.inso.entity.Course;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CourseNormalizer implements Normalizer {

    @Override
    public void normalize(Map<Course, Double> courses) {
        double max = courses.values().stream().collect(Collectors.summarizingDouble(Double::doubleValue)).getMax();
        double min = courses.values().stream().collect(Collectors.summarizingDouble(Double::doubleValue)).getMin();

        if (max - min == 0)
            return;

        courses.keySet().forEach(course -> {
                    double value = (courses.get(course) - min) / (max - min);
                    courses.put(course, value);
                }
        );
    }
}
