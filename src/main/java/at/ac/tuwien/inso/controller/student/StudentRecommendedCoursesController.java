package at.ac.tuwien.inso.controller.student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

import at.ac.tuwien.inso.controller.Constants;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.UserAccountService;
import at.ac.tuwien.inso.service.course_recommendation.RecommendationService;

@Controller
@RequestMapping("/student/recommended")
public class StudentRecommendedCoursesController {

    private static final Logger log = LoggerFactory.getLogger(StudentMyCoursesController.class);


    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private RecommendationService recommendationService;



    @ModelAttribute("recommendedCourses")
    private List<Course> getRecommendedCourses(Principal principal) {

        log.info("Getting recommendation for student: [{}]", principal.getName());

        Student student = getLoggedInStudent(principal);
        return recommendationService.recommendCoursesSublist(student);
    }


    @GetMapping
    public String courses() {
        return "/student/recommended";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String dismissCourse(Principal principal, Long dismissedId) {
        log.info("Post with [{}] as request body", dismissedId);
        Student student = getLoggedInStudent(principal);
        courseService.dismissCourse(student, dismissedId);
        return "redirect:/student/recommended";
    }


    private Student getLoggedInStudent(Principal principal) {
        Student student = studentService.findByUsername(principal.getName());
        return student;
    }

}
