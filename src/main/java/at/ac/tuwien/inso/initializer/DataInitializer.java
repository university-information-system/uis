package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class DataInitializer {

	private final static int DEFAULT_ECTS_NUMBER = 3;

	@Bean
	CommandLineRunner initialize(CourseRepository courseRepository) {
		return String -> courseRepository.save(createCourses());
	}

	private List<Course> createCourses() {
		return Arrays.stream("ASE, SEPM, Advanced Internet Computing, Introduction to security, OOP, Seminar".split(","))
				.map(name -> new Course(name.replace(", ", ""), DEFAULT_ECTS_NUMBER))
				.collect(Collectors.toList());
	}

}
