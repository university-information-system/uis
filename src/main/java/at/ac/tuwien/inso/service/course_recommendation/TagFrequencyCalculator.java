package at.ac.tuwien.inso.service.course_recommendation;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Tag;

import java.util.Map;

public interface TagFrequencyCalculator {

    Map<Tag, Double> calculate(Student student);
}
