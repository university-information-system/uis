package at.ac.tuwien.inso.restController;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/courses")
public class CourseRESTController {

	@Autowired
	private CourseRepository courseRepository;

	@GetMapping
	private List<Course> getCourses() {
		return courseRepository.findAll();
	}

	@GetMapping("/{id}")
	private Course getCourse(@PathVariable String id) {
		return courseRepository.findOne(Long.parseLong(id));
	}

	@PostMapping
	private Course createCourse(@RequestBody Course course) {
		return courseRepository.save(course);
	}

	@PutMapping("/{id}")
	private Course updateCourse(@RequestBody Course course,
	                            @PathVariable String id) {
		course.setId(Long.parseLong(id));
		return courseRepository.save(course);
	}

	@DeleteMapping("/{id}")
	private void deleteCourse(@PathVariable String id) {
		courseRepository.delete(Long.parseLong(id));
	}



}
