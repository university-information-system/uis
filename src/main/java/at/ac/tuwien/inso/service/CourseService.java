package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.entity.*;

import javax.validation.constraints.*;
import java.util.*;

public interface CourseService {
    List<Course> findCourseForCurrentSemesterWithName(@NotNull String name);

    List<Course> findCoursesForCurrentSemesterForLecturer(Lecturer lecturer);

    Course saveCourse(AddCourseForm form);

    Course findOne(Long id);

    boolean registerStudentForCourse(Course course);
}
