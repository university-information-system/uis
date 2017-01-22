package at.ac.tuwien.inso.controller.student;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.ac.tuwien.inso.controller.Constants;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.UserAccountService;
import at.ac.tuwien.inso.service.course_recommendation.RecommendationService;

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
    private Page<Course> getAllCourses(@RequestParam(value = "search", defaultValue = "") String search,
                                       @PageableDefault Pageable pageable) {
        if (pageable.getPageSize() > Constants.MAX_PAGE_SIZE) {
            pageable = new PageRequest(pageable.getPageNumber(), Constants.MAX_PAGE_SIZE);
        }

        return courseService.findCourseForCurrentSemesterWithName(search, pageable);
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
