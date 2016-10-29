package at.ac.tuwien.inso.controller;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller()
@RequestMapping("/")
public class HomeController {

    @Inject
    CourseRepository courseRepository;

    @GetMapping
    public String home() {
        return "home";
    }

    @ModelAttribute("listOfCourses")
    public List<Course> listCourses() {
        return courseRepository.findAll();
    }

    @RequestMapping(value = "/addcourse", method = RequestMethod.POST)
    public String addCourse(@RequestParam("name") String courseName) {
        courseRepository.save(new Course(courseName));
        return "redirect:/";
    }
}
