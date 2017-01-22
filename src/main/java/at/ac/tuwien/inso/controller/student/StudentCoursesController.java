package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.controller.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.course_recommendation.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.web.*;
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
    private StudentService studentService;

    @ModelAttribute("allCourses")
    private Page<Course> getAllCourses(@RequestParam(value = "search", defaultValue = "") String search, @PageableDefault Pageable pageable) {
        if (pageable.getPageSize() > Constants.MAX_PAGE_SIZE) {
            pageable = new PageRequest(pageable.getPageNumber(), Constants.MAX_PAGE_SIZE);
        }

        return courseService.findCourseForCurrentSemesterWithName(search, pageable);
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
    public String course(@PathVariable Long id, Model model, Principal principal) {
        log.debug("Student " + principal.getName() + " requesting course " + id + " details");

        Student student = studentService.findByUsername(principal.getName());

        model.addAttribute("course", courseService.courseDetailsFor(student, id));

        return "/student/course-details";
    }

}
