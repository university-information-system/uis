package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

import static java.util.Arrays.*;

@Configuration
@Profile("demo")
public class DataInitializer {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UisUserRepository uisUserRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    private List<Student> students;

    private List<Lecturer> lecturers;

    @Bean
    CommandLineRunner initialize() {
        return String -> {

            userAccountRepository.save(new UserAccount("admin", "pass", Role.ADMIN));

            createUsers();

            //create semesters
            Semester ss2016 = semesterRepository.save(new Semester("SS2016"));
            Semester ws2016 = semesterRepository.save(new Semester("WS2016"));

            //subjects
            Subject calculus = subjectRepository.save(new Subject("Calculus", new BigDecimal(3.0)));
            calculus.addLecturers(lecturers.get(3));
            Subject sepm = subjectRepository.save(new Subject("SEPM", new BigDecimal(6.0)));
            sepm.addLecturers(lecturers.get(3));
            Subject ase = subjectRepository.save(new Subject("ASE", new BigDecimal(6.0)));
            ase.addRequiredSubjects(sepm);
            ase.addLecturers(lecturers.get(3), lecturers.get(4));
            subjectRepository.save(ase);
            subjectRepository.save(sepm);
            subjectRepository.save(calculus);

            //courses
            Course sepmSS2016 = courseRepository.save(new Course(sepm,ss2016));
            Course sepmWS2016 = courseRepository.save(new Course(sepm,ws2016));
            sepmWS2016.addStudents(students.get(3));
            Course aseWS2016 = courseRepository.save(new Course(ase,ws2016));
            aseWS2016.addStudents(students.get(0), students.get(1), students.get(2), students.get(3));
            Course calculusWS2016 = courseRepository.save(new Course(calculus, ws2016));
            calculusWS2016.addStudents(students.get(0), students.get(3));

            //study plan
            StudyPlan studyPlan = studyPlanRepository.save(new StudyPlan("SE",new EctsDistribution(new BigDecimal(90), new BigDecimal(60), new BigDecimal(30))));

            SubjectForStudyPlan subjectForStudyPlan1 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(calculus, false));
            SubjectForStudyPlan subjectForStudyPlan2 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(ase, true));
            SubjectForStudyPlan subjectForStudyPlan3 = subjectForStudyPlanRepository.save(new SubjectForStudyPlan(sepm, true));
            studyPlan.addSubjects(subjectForStudyPlan1, subjectForStudyPlan2, subjectForStudyPlan3);
        };
    }

    private void createUsers() {
        Iterable<UisUser> users = uisUserRepository.save(asList(
                new Student("Emma Dowd", "emma.dowd@gmail.com", new UserAccount("emma", "pass", Role.STUDENT)),
                new Lecturer("Carol Sanderson", "carol@uis.at"),
                new Lecturer("Una Walker", "una.walker@uis.at", new UserAccount("lecturer", "pass", Role.LECTURER)),
                new Student("Caroline Black", "caroline.black@uis.at", new UserAccount("student", "pass", Role.STUDENT)),
                new Student("Joan Watson", "joan.watson@uit.at"),
                new Lecturer("Connor MacLeod", "connor@gmail.com"),
                new Student("James Bond", "jamesbond_007@yahoo.com"),
                new Student("Trevor Bond", "trevor@uis.at"),
                new Lecturer("Eric Wilkins", "e1234567@tuwien.ac.at", new UserAccount("eric", "pass", Role.LECTURER)),
                new Lecturer("Benjamin Piper", "ben@uis.at", new UserAccount("ben", "pass", Role.LECTURER))
        ));

        students = StreamSupport.stream(users.spliterator(), false)
                .filter(it -> it instanceof Student)
                .map(it -> (Student) it)
                .collect(Collectors.toList());

        lecturers = StreamSupport.stream(users.spliterator(), false)
                .filter(it -> it instanceof Lecturer)
                .map(it -> (Lecturer) it)
                .collect(Collectors.toList());
    }
}
