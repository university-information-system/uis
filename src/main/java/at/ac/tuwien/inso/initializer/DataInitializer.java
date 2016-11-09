package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;

import java.math.*;

@Configuration
@Profile("demo")
public class DataInitializer {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    @Bean
    CommandLineRunner initialize() {
        return String -> {

            userAccountRepository.save(new UserAccount("admin", "pass", Role.ADMIN));

            //users
            Lecturer lecturer1 = lecturerRepository.save(new Lecturer("Lecturer 1", "email", new UserAccount("lecturer1", "pass", Role.LECTURER)));
            Student student1 = studentRepository.save(new Student("Student 1", "email", new UserAccount("student1", "pass", Role.STUDENT)));
            Lecturer lecturer2 = lecturerRepository.save(new Lecturer("Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER)));
            Student student2 = studentRepository.save(new Student("Student 2", "email", new UserAccount("student2", "pass", Role.STUDENT)));
            Student student3 = studentRepository.save(new Student("Student 3", "email", new UserAccount("student3", "pass", Role.STUDENT)));
            Lecturer lecturer3 = lecturerRepository.save(new Lecturer("Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER)));
            Lecturer lecturer4 = lecturerRepository.save(new Lecturer("Lecturer 3", "email"));
            Student student4 = studentRepository.save(new Student("Student 4", "email", new UserAccount("student4", "pass", Role.STUDENT)));
            Student student5 = studentRepository.save(new Student("Student 4", "email"));

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
