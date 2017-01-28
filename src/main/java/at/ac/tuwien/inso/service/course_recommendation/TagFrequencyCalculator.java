package at.ac.tuwien.inso.service.course_recommendation;

import java.util.Map;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Tag;

public interface TagFrequencyCalculator {

    /**
     * Calculates the amount of times a tag occurs, with special distinction made between courses
     * where the student has given feedback, courses for which the student has a grade and
     * all other courses.
     *
     * @param student The student for whom the calculation is performed
     * @return a map containing the Tags and the frequency with which they occur
     */
    Map<Tag, Double> calculate(Student student);
}
