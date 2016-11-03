package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.*;

import java.util.*;
import java.util.stream.*;

import static java.util.Arrays.*;

@Configuration
@Profile("demo")
public class DataInitializer {

    private final static int DEFAULT_ECTS_NUMBER = 3;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Bean
    CommandLineRunner initialize() {
        return String -> {
            courseRepository.save(createCourses());

            createUsers().forEach(it -> userAccountService.create(it));
        };
    }

    private List<Course> createCourses() {
        return Arrays.stream("ASE, SEPM, Advanced Internet Computing, Introduction to security, OOP, Seminar".split(","))
                .map(name -> new Course(name.replace(", ", ""), DEFAULT_ECTS_NUMBER))
                .collect(Collectors.toList());
    }

    private List<UserAccount> createUsers() {
        return asList(
                new UserAccount("admin", "pass", new Role("ROLE_ADMIN")),
                new UserAccount("lecturer", "pass", new Role("ROLE_LECTURER")),
                new UserAccount("student", "pass", new Role("ROLE_STUDENT"))
        );
    }
}
