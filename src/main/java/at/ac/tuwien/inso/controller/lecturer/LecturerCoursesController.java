package at.ac.tuwien.inso.controller.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.LecturerService;

@Controller
@RequestMapping("/lecturer/courses")
public class LecturerCoursesController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private LecturerService lecturerService;


    @ModelAttribute("allCourses")
    private List<Course> getAllCourses() {
        Lecturer lecturer = lecturerService.getLoggedInLecturer();
        return courseService.findCoursesForCurrentSemesterForLecturer(lecturer);
    }

    @GetMapping
    public String courses() {
        return "lecturer/courses";
    }
}
