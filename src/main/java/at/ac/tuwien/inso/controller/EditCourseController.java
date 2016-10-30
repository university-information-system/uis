package at.ac.tuwien.inso.controller;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EditCourseController {

	@Autowired
	private CourseRepository courseRepository;

	@ModelAttribute("course")
	public Course getCourse(@RequestParam("id") long id) {
		return courseRepository.findOne(id);
	}

	@GetMapping
	public String getEditCourse() {
		return "editCourse";
	}

	@PostMapping
	public String updateCourse(@ModelAttribute Course course) {
		courseRepository.save(course);
		return "redirect:/";
	}

}
