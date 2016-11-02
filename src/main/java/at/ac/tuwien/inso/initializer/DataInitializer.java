package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.*;

import java.util.*;
import java.util.stream.*;

import static java.util.Arrays.*;

@Configuration
public class DataInitializer {

	private final static int DEFAULT_ECTS_NUMBER = 3;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;

	@Bean
	CommandLineRunner initialize() {
		return String -> {
			courseRepository.save(createCourses());

			userRepository.save(createUsers());
		};
	}

	private List<Course> createCourses() {
		return Arrays.stream("ASE, SEPM, Advanced Internet Computing, Introduction to security, OOP, Seminar".split(","))
				.map(name -> new Course(name.replace(", ", ""), DEFAULT_ECTS_NUMBER))
				.collect(Collectors.toList());
	}

	private List<User> createUsers() {
		String password = new StandardPasswordEncoder().encode("pass");
		return asList(
				new User("admin", password, new Role("ROLE_ADMIN")),
				new User("lecturer", password, new Role("ROLE_LECTURER")),
				new User("student", password, new Role("ROLE_STUDENT"))
		);
	}
}
