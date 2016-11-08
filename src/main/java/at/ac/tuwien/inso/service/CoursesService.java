package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.validation.constraints.NotNull;

@Service
public class CoursesService {

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private CourseRepository courseRepository;


    public List<Course> findCourseForCurrentSemesterWithName(@NotNull String name) {
        Semester semester = semesterService.getCurrentSemester();
        return courseRepository.findAllBySemesterAndSubjectNameLikeIgnoreCase(semester, "%" + name + "%");
    }

}
