package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;

@Configuration
@Profile("demo")
public class DataInitializer {

    @Autowired
    private CourseRepository courseRepository;
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
    @Autowired
    private TagRepository tagRepository;

    private List<StudyPlan> studyplans;

    private List<Semester> semesters;

    private List<Subject> subjects;

    private List<Course> courses;

    private List<Student> students;

    private List<Lecturer> lecturers;

    @Bean
    CommandLineRunner initialize() {
        return String -> {

            userAccountRepository.save(new UserAccount("admin", "pass", Role.ADMIN));

            createUsers();

            createSemesters();

            createSubjects();

            createCourses();

            createStudyPlans();

            registerStudentsToStudyPlans();

            addPreconditionsToSubjects();

            registerSubjectsToLecturers();

            registerCoursesToStudents();

            createTags();

            addSubjectsToStudyPlans();
        };
    }

    private void createTags() {
        tagRepository.save(asList(
                new Tag("Computer Science"),
                new Tag("Math"),
                new Tag("Fun"),
                new Tag("Easy"),
                new Tag("Difficult")
        ));
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

    private void createSemesters() {
        Iterable<Semester> semesters = semesterRepository.save(asList(
                new Semester("SS2016"),
                new Semester("WS2016")
        ));

        this.semesters = StreamSupport.stream(semesters.spliterator(), false).collect(Collectors.toList());
    }

    private void createSubjects() {
        Iterable<Subject> subjects = subjectRepository.save(asList(
                new Subject("Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik", new BigDecimal(3.0)),
                new Subject("Software Engineering and Project Management", new BigDecimal(6.0)),
                new Subject("Advanced Software Engineering", new BigDecimal(6.0)),
                new Subject("Digital forensics", new BigDecimal(3.0)),
                new Subject("Model Engineering", new BigDecimal(6.0)),
                new Subject("Formale Methoden", new BigDecimal(6.0)),
                new Subject("Datenbanksysteme", new BigDecimal(6.0)),
                new Subject("Verteile Systeme", new BigDecimal(3.0))
        ));

        this.subjects = StreamSupport.stream(subjects.spliterator(), false).collect(Collectors.toList());
    }

    private void createCourses() {
        Iterable<Course> courses = courseRepository.save(asList(
                new Course(subjects.get(0), semesters.get(0)),
                new Course(subjects.get(0), semesters.get(1)),
                new Course(subjects.get(2), semesters.get(1)),
                new Course(subjects.get(0), semesters.get(1))
        ));

        this.courses = StreamSupport.stream(courses.spliterator(), false).collect(Collectors.toList());
    }

    private void createStudyPlans() {
        Iterable<StudyPlan> studyplans = studyPlanRepository.save(asList(
                new StudyPlan("Bachelor Software and Information Engineering", new EctsDistribution(new BigDecimal(90), new BigDecimal(60), new BigDecimal(30))),
                new StudyPlan("Master Business Informatics", new EctsDistribution(new BigDecimal(30), new BigDecimal(70), new BigDecimal(20))),
                new StudyPlan("Master Computational Intelligence", new EctsDistribution(new BigDecimal(60),new BigDecimal(30),new BigDecimal(30))),
                new StudyPlan("Master Visual Computing", new EctsDistribution(new BigDecimal(60),new BigDecimal(30),new BigDecimal(30))),
                new StudyPlan("Master Medical Informatics", new EctsDistribution(new BigDecimal(60),new BigDecimal(30),new BigDecimal(30))),
                new StudyPlan("Master Computer Science", new EctsDistribution(new BigDecimal(60),new BigDecimal(30),new BigDecimal(30)))
        ));

        this.studyplans = StreamSupport.stream(studyplans.spliterator(), false).collect(Collectors.toList());
    }

    private void registerStudentsToStudyPlans() {
        students.stream()
                .limit(2)
                .forEach(it -> {
                    it.addStudyplans(new StudyPlanRegistration(studyplans.get(0), semesters.get(0)));
                    uisUserRepository.save(it);
                });

        students.stream()
                .skip(2)
                .forEach(it -> {
                    it.addStudyplans(
                            new StudyPlanRegistration(studyplans.get(0), semesters.get(0)),
                            new StudyPlanRegistration(studyplans.get(1), semesters.get(1))
                    );
                    uisUserRepository.save(it);
                });
    }

    private void addPreconditionsToSubjects() {
        subjects.get(2).addRequiredSubjects(subjects.get(1));
    }

    private void registerSubjectsToLecturers() {
        subjects.get(0).addLecturers(lecturers.get(3));
        subjects.get(1).addLecturers(lecturers.get(3));
        subjects.get(2).addLecturers(lecturers.get(3), lecturers.get(4));

        subjectRepository.save(subjects);
    }

    private void registerCoursesToStudents() {
        courses.get(1).addStudents(students.get(3));
        courses.get(2).addStudents(students.get(0), students.get(1), students.get(2), students.get(3));
        courses.get(3).addStudents(students.get(0), students.get(3));

        courseRepository.save(courses);
    }

    private void addSubjectsToStudyPlans() {
        subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(0), studyplans.get(0), true, 1));
        subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(1), studyplans.get(0), true, 1));
        subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(2), studyplans.get(0), true, 2));
        subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(3), studyplans.get(0), false, 3));
        subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(4), studyplans.get(0), false, 2));
        subjectForStudyPlanRepository.save(new SubjectForStudyPlan(subjects.get(5), studyplans.get(0), true, 2));
    }
}
