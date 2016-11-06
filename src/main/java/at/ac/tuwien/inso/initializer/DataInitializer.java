package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;

@Configuration
@Profile("demo")
public class DataInitializer {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Bean
    CommandLineRunner initialize() {
        return String -> {
            userAccountRepository.save(new UserAccount("admin", "pass", new Role("ROLE_ADMIN")));

            lecturerRepository.save(new Lecturer(new UserProfile("Lecturer 1", "email", new UserAccount("lecturer", "pass", new Role("ROLE_LECTURER")))));
            studentRepository.save(new Student(new UserProfile("Student 1", "email", new UserAccount("student", "pass", new Role("ROLE_STUDENT")))));
        };
    }
}
