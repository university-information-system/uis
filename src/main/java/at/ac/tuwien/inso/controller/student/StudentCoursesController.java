package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.service.CoursesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student/courses")
public class StudentCoursesController {

    @Autowired
    private CoursesService coursesService;

    @ModelAttribute("allCourses")
    private List<Course> getAllCourses(@RequestParam(value = "search", required = false) String search) {
        final String searchString = getSearchString(search);
        return coursesService.findCourseForCurrentSemesterWithName(searchString);
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
