package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.service.CoursesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student/courses")
public class StudentCoursesController {

    @Autowired
    private CoursesService coursesService;

    @ModelAttribute("allCourses")
    private List<Course> getAllCourses() {
        return coursesService.findForCurrentSemester();
    }

    @GetMapping
    public String courses() {
        return "/student/courses";
    }
}
