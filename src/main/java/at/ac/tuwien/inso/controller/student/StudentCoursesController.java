package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.course_recommendation.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.*;

@Controller
@RequestMapping("/student/courses")
public class StudentCoursesController {

    private static final Logger log = LoggerFactory.getLogger(StudentMyCoursesController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RecommendationService recommendationService;

    @ModelAttribute("allCourses")
    private List<Course> getAllCourses(@RequestParam(value = "search", defaultValue = "") String search) {
        return courseService.findCourseForCurrentSemesterWithName(search);
    }

    @ModelAttribute("recommendedCourses")
    private List<Course> getRecommendedCourses(Principal principal) {
        Student student = studentService.findByUsername(principal.getName());

        return recommendationService.recommendCourses(student);
    }

    @ModelAttribute("searchString")
    private String getSearchString(@RequestParam(value = "search", defaultValue = "") String search) {
        return search;
    }

    @GetMapping
    public String courses() {
        return "/student/courses";
    }

    @GetMapping("/{id}")
    public String course(@PathVariable Long id,
                         Model model,
                         Principal principal) {
        log.debug("Student " + principal.getName() + " requesting course " + id + " details");

        Student student = studentService.findByUsername(principal.getName());

        model.addAttribute("course", courseService.courseDetailsFor(student, id));

        return "/student/course-details";
    }

}
