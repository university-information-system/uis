package at.ac.tuwien.inso.controller;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller()
@RequestMapping("/")
public class HomeController {

    @Autowired
    private CourseRepository courseRepository;

    @ModelAttribute("listOfCourses")
    public List<Course> listCourses() {
        return courseRepository.findAll();
    }

    @GetMapping
    public String home() {
        return "home";
    }

    @PostMapping(value = "/addcourse")
    public String addCourse(@RequestParam("name") String courseName) {
        courseRepository.save(new Course(courseName));
        return "redirect:/";
    }
}
