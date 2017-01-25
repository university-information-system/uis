package at.ac.tuwien.inso.service.validator;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.exception.ValidationException;

public class CourseValidator {

    public void validateNewCourse(Course course) {

        validateCourse(course);

        if(course.getStudentLimits() < 1) {
            throw new ValidationException("The student limit of the course needs to be at least 1");
        }

    }

    public void validateCourse(Course course) {

        if(course == null || course.getSubject() == null) {
            throw new ValidationException("Course not found");
        }

    }

    public void validateCourseId(Long id) {

        if(id == null || id < 1) {
            throw new ValidationException("Course invalid id");
        }

    }

    public void validateStudent(Student student) {

        if(student == null) {
            throw new ValidationException("Student not found");
        }

    }
}
