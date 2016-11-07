package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;

import java.math.BigDecimal;

@Configuration
@Profile("demo")
public class DataInitializer {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired CourseRepository courseRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    @Bean
    CommandLineRunner initialize() {
        return String -> {

            // create roles
            Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
            Role lecturerRole = roleRepository.save(new Role("ROLE_LECTURER"));
            Role studentRole = roleRepository.save(new Role("ROLE_STUDENT"));

            //the admin
            userAccountRepository.save(new UserAccount("admin", "pass", adminRole));

            //lecturers
            Lecturer lecturer1 = lecturerRepository.save(new Lecturer(new UserProfile("Lecturer 1", "email", new UserAccount("lecturer1", "pass",lecturerRole))));
            Lecturer lecturer2 = lecturerRepository.save(new Lecturer(new UserProfile("Lecturer 2", "email", new UserAccount("lecturer2", "pass", lecturerRole))));
            Lecturer lecturer3 = lecturerRepository.save(new Lecturer(new UserProfile("Lecturer 3", "email", new UserAccount("lecturer3", "pass", lecturerRole))));

            //students
            Student student1 = studentRepository.save(new Student(new UserProfile("Student 1", "email", new UserAccount("student1", "pass", studentRole))));
            Student student2 = studentRepository.save(new Student(new UserProfile("Student 2", "email", new UserAccount("student2", "pass", studentRole))));
            Student student3 = studentRepository.save(new Student(new UserProfile("Student 3", "email", new UserAccount("student3", "pass", studentRole))));
            Student student4 = studentRepository.save(new Student(new UserProfile("Student 4", "email", new UserAccount("student4", "pass", studentRole))));

            //create semesters
            Semester ss2016 = semesterRepository.save(new Semester("SS2016"));
            Semester ws2016 = semesterRepository.save(new Semester("WS2016"));

            //subjects
            Subject calculus = subjectRepository.save(new Subject("Calculus", new BigDecimal(3.0)));
            calculus.addLecturers(lecturer3);
            Subject sepm = subjectRepository.save(new Subject("SEPM", new BigDecimal(6.0)));
            sepm.addLecturers(lecturer1);
            Subject ase = subjectRepository.save(new Subject("ASE", new BigDecimal(6.0)));
            ase.addRequiredSubjects(sepm);
            ase.addLecturers(lecturer1, lecturer2);

            //courses
            Course sepmSS2016 = courseRepository.save(new Course(sepm,ss2016));
            Course sepmWS2016 = courseRepository.save(new Course(sepm,ws2016));
            sepmWS2016.addStudents(student4);
            Course aseWS2016 = courseRepository.save(new Course(ase,ws2016));
            aseWS2016.addStudents(student1, student2, student3, student4);
            Course calculusWS2016 = courseRepository.save(new Course(calculus, ws2016));
            calculusWS2016.addStudents(student1, student4);

            //study plan
            StudyPlan studyPlan = studyPlanRepository.save(new StudyPlan("SE",new EctsDistribution(new BigDecimal(90), new BigDecimal(60), new BigDecimal(30))));

            SubjectForStudyPlan subjectForStudyPlan1 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(calculus, false));
            SubjectForStudyPlan subjectForStudyPlan2 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(ase, true));
            SubjectForStudyPlan subjectForStudyPlan3 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(sepm, true));
            studyPlan.addSubjects(subjectForStudyPlan1, subjectForStudyPlan2, subjectForStudyPlan3);


        };
    }
}
