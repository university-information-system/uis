package at.ac.tuwien.inso.service.course_recommendation;

import java.util.Map;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Tag;

public interface TagFrequencyCalculator {

    Map<Tag, Double> calculate(Student student);
}
