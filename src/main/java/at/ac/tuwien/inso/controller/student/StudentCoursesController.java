package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.UserAccountService;
import at.ac.tuwien.inso.service.course_recommendation.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/student/courses")
public class StudentCoursesController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RecommendationService recommendationService;

    @ModelAttribute("allCourses")
    private List<Course> getAllCourses(@RequestParam(value = "search", required = false) String search) {
        if (search == null || search.isEmpty()) {
            Student student = studentService.findOne(userAccountService.getCurrentLoggedInUser());
            List<Course> recommendedCourses = recommendationService.recommendCourses(student);
            if (!recommendedCourses.isEmpty()) {
                return recommendedCourses;
            }
        }

        final String searchString = getSearchString(search);
        return courseService.findCourseForCurrentSemesterWithName(searchString);
    }

    @ModelAttribute("searchString")
    private String getSearchString(@RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return search;
        }

        return "";
    }

    @GetMapping
    public String courses() {
        return "/student/courses";
    }

}
