package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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

    private HashMap<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester1 = new HashMap<String, Subject>() {
        {
            put("VU Programmkonstruktion", new Subject("VU Programmkonstruktion", new BigDecimal(8.8)));
            put("UE Studieneingangsgespräch", new Subject("UE Studieneingangsgespräch", new BigDecimal(0.2)));
            put("VU Technische Grundlagen der Informatik", new Subject("VU Technische Grundlagen der Informatik", new BigDecimal(6.0)));
            put("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik", new Subject("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik", new BigDecimal(4.0)));
            put("UE Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik", new Subject("UE Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik", new BigDecimal(5.0)));
            put("VU Formale Modellierung", new Subject("VU Formale Modellierung", new BigDecimal(3.0)));
            put("VU Datenmodellierung", new Subject("VU Datenmodellierung", new BigDecimal(3.0)));
        }
    };

    private HashMap<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester2 = new HashMap<String, Subject>() {
        {
            put("VU Algorithmen und Datenstrukturen 1", new Subject("VU Algorithmen und Datenstrukturen 1", new BigDecimal(6.0)));
            put("VU Algorithmen und Datenstrukturen 2", new Subject("VU Algorithmen und Datenstrukturen 2", new BigDecimal(3.0)));
            put("VU Einführung in Visual Computing", new Subject("VU Einführung in Visual Computing", new BigDecimal(6.0)));
            put("VU Gesellschaftliche Spannungsfelder der Informatik", new Subject("VU Gesellschaftliche Spannungsfelder der Informatik", new BigDecimal(3.0)));
            put("VU Basics of Human Computer Interaction", new Subject("VU Basics of Human Computer Interaction", new BigDecimal(3.0)));
            put("VO Analysis für Informatik und Wirtschaftsinformatik", new Subject("VO Analysis für Informatik und Wirtschaftsinformatik", new BigDecimal(2.0)));
            put("UE Analysis für Informatik und Wirtschaftsinformatik", new Subject("UE Analysis für Informatik und Wirtschaftsinformatik", new BigDecimal(4.0)));
            put("VU Objektorientierte Modellierung", new Subject("VU Objektorientierte Modellierung", new BigDecimal(3.0)));
        }
    };

    private HashMap<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester3 = new HashMap<String, Subject>() {
        {
            put("VU Objektorientierte Programmiertechniken", new Subject("VU Objektorientierte Programmiertechniken", new BigDecimal(3.0)));
            put("VU Funktionale Programmierung", new Subject("VU Funktionale Programmierung", new BigDecimal(3.0)));
            put("VO Betriebssysteme", new Subject("VO Betriebssysteme", new BigDecimal(2.0)));
            put("UE Betriebssysteme", new Subject("UE Betriebssysteme", new BigDecimal(4.0)));
            put("VU Introduction to Security", new Subject("VU Introduction to Security", new BigDecimal(3.0)));
            put("VU Daten- und Informatikrecht", new Subject("VU Daten- und Informatikrecht", new BigDecimal(3.0)));
            put("VU Datenbanksysteme", new Subject("VU Datenbanksysteme", new BigDecimal(6.0)));
            put("VO Statistik und Wahrscheinlichkeitstheorie", new Subject("VO Statistik und Wahrscheinlichkeitstheorie", new BigDecimal(3.0)));
            put("UE Statistik und Wahrscheinlichkeitstheorie", new Subject("UE Statistik und Wahrscheinlichkeitstheorie", new BigDecimal(3.0)));
        }
    };

    private HashMap<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester4 = new HashMap<String, Subject>() {
        {
            put("VU Einführung in die Künstliche Intelligenz", new Subject("VU Einführung in die Künstliche Intelligenz", new BigDecimal(3.0)));
            put("VU Theoretische Informatik und Logik", new Subject("VU Theoretische Informatik und Logik", new BigDecimal(6.0)));
            put("VO Software Engineering und Projektmanagement", new Subject("VO Software Engineering und Projektmanagement", new BigDecimal(3.0)));
            put("PR Software Engineering und Projektmanagement", new Subject("PR Software Engineering und Projektmanagement", new BigDecimal(6.0)));
        }
    };

    private HashMap<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester5 = new HashMap<String, Subject>() {
        {
            put("VO Verteilte Systeme", new Subject("VO Verteilte Systeme", new BigDecimal(3.0)));
            put("UE Verteilte Systeme", new Subject("UE Verteilte Systeme", new BigDecimal(3.0)));
            put("VU Gesellschaftswissenschaftliche Grundlagen der Informatik", new Subject("VU Gesellschaftswissenschaftliche Grundlagen der Informatik", new BigDecimal(3.0)));
            put("VU Interface and Interaction Design", new Subject("VU Interface and Interaction Design", new BigDecimal(3.0)));
            put("VU Einführung in wissensbasierte Systeme", new Subject("VU Einführung in wissensbasierte Systeme", new BigDecimal(3.0)));
            put("SE Wissenschaftliches Arbeiten", new Subject("SE Wissenschaftliches Arbeiten", new BigDecimal(3.0)));
        }
    };

    private HashMap<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester6 = new HashMap<String, Subject>() {
        {
            put("PR Bachelorarbeit für Informatik und Wirtschaftsinformatik", new Subject("PR Bachelorarbeit für Informatik und Wirtschaftsinformatik", new BigDecimal(10.0)));
        }
    };

    private HashMap<String, Subject> subjectsOptionalBachelorSoftwareAndInformationEngineering = new HashMap<String, Subject>() {
        {
            put("VU Propädeutikum für Informatik", new Subject("VU Propädeutikum für Informatik", new BigDecimal(6.0)));
            put("VO Deklaratives Problemlösen", new Subject("VO Deklaratives Problemlösen", new BigDecimal(3.0)));
            put("UE Deklaratives Problemlösen", new Subject("UE Deklaratives Problemlösen", new BigDecimal(3.0)));
            put("VU Logikprogrammierung und Constraints", new Subject("VU Logikprogrammierung und Constraints", new BigDecimal(6.0)));
            put("VO Abstrakte Maschinen", new Subject("VO Abstrakte Maschinen", new BigDecimal(3.0)));
            put("UE Abstrakte Maschinen", new Subject("UE Abstrakte Maschinen", new BigDecimal(3.0)));
            put("VU Microcontroller", new Subject("VU Microcontroller", new BigDecimal(7.0)));
            put("UE Programmierung von Betriebssystemen", new Subject("UE Programmierung von Betriebssystemen", new BigDecimal(3.0)));
            put("VU Übersetzerbau", new Subject("VU Übersetzerbau", new BigDecimal(6.0)));
            put("VO Echtzeitsysteme", new Subject("VO Echtzeitsysteme", new BigDecimal(2.0)));
            put("VU Dependable Systems", new Subject("VU Dependable Systems", new BigDecimal(4.0)));
            put("VU Internet Security", new Subject("VU Internet Security", new BigDecimal(3.0)));
            put("VU Internet Security", new Subject("VU Internet Security", new BigDecimal(3.0)));
            put("VU Privacy Enhancing Technology", new Subject("VU Privacy Enhancing Technology", new BigDecimal(3.0)));
            put("UE Daten- und Informatikrecht", new Subject("UE Daten- und Informatikrecht", new BigDecimal(3.0)));
            put("VU Vertrags- und Haftungsrecht", new Subject("VU Vertrags- und Haftungsrecht", new BigDecimal(3.0)));
            put("VU Semistrukturierte Daten", new Subject("VU Semistrukturierte Daten", new BigDecimal(3.0)));
            put("VU Web Engineering", new Subject("VU Web Engineering", new BigDecimal(4.5)));
            put("VU Computerstatistik", new Subject("VU Computerstatistik", new BigDecimal(3.0)));
            put("VU Datenanalyse", new Subject("VU Datenanalyse", new BigDecimal(3.0)));
            put("VU Statistical Computing", new Subject("VU Statistical Computing", new BigDecimal(3.0)));
            put("VO Logik für Wissensrepräsentation", new Subject("VO Logik für Wissensrepräsentation", new BigDecimal(3.0)));
            put("UE Logik für Wissensrepräsentation", new Subject("UE Logik für Wissensrepräsentation", new BigDecimal(3.0)));
            put("VO Computernumerik", new Subject("VO Computernumerik", new BigDecimal(3.0)));
            put("UE Computernumerik", new Subject("UE Computernumerik", new BigDecimal(1.5)));
            put("VO Multivariate Statistik", new Subject("VO Multivariate Statistik", new BigDecimal(4.5)));
            put("UE Multivariate Statistik", new Subject("UE Multivariate Statistik", new BigDecimal(1.5)));
            put("VU Statistische Simulation und computerintensive Methoden", new Subject("VU Statistische Simulation und computerintensive Methoden", new BigDecimal(3.0)));
            put("VU Argumentieren und Beweisen", new Subject("VU Argumentieren und Beweisen", new BigDecimal(6.0)));
            put("VU Programm- und Systemverifikation", new Subject("VU Programm- und Systemverifikation", new BigDecimal(6.0)));
            put("VU Softwareprojekt-Beobachtung und -Controlling", new Subject("VU Softwareprojekt-Beobachtung und -Controlling", new BigDecimal(6.0)));
            put("VU Software-Qualitätssicherung", new Subject("VU Software-Qualitätssicherung", new BigDecimal(6.0)));
            put("VU Usability Engineering", new Subject("VU Usability Engineering", new BigDecimal(3.0)));
            put("SE Coaching als Führungsinstrument 2", new Subject("SE Coaching als Führungsinstrument 2", new BigDecimal(3.0)));
            put("SE Didaktik in der Informatik", new Subject("SE Didaktik in der Informatik", new BigDecimal(3.0)));
            put("VO EDV-Vertragsrecht", new Subject("VO EDV-Vertragsrecht", new BigDecimal(1.5)));
            put("VO Einführung in Technik und Gesellschaft", new Subject("VO Einführung in Technik und Gesellschaft", new BigDecimal(3.0)));
            put("SE Folgenabschätzung von Informationstechnologien", new Subject("SE Folgenabschätzung von Informationstechnologien", new BigDecimal(3.0)));
            put("VU Forschungsmethoden", new Subject("VU Forschungsmethoden", new BigDecimal(3.0)));
            put("VU Kommunikation und Moderation", new Subject("VU Kommunikation und Moderation", new BigDecimal(3.0)));
            put("VU Kooperatives Arbeiten", new Subject("VU Kooperatives Arbeiten", new BigDecimal(3.0)));
            put("SE Rechtsinformationsrecherche im Internet", new Subject("SE Rechtsinformationsrecherche im Internet", new BigDecimal(3.0)));
            put("VU Softskills für TechnikerInnen", new Subject("VU Softskills für TechnikerInnen", new BigDecimal(3.0)));
            put("VU Techniksoziologie und Technikpsychologie", new Subject("VU Techniksoziologie und Technikpsychologie", new BigDecimal(3.0)));
            put("VO Theorie und Praxis der Gruppenarbeit", new Subject("VO Theorie und Praxis der Gruppenarbeit", new BigDecimal(3.0)));
            put("SE Wissenschaftliche Methodik", new Subject("SE Wissenschaftliche Methodik", new BigDecimal(3.0)));
            put("SE Scientific Presentation and Communication", new Subject("SE Scientific Presentation and Communication", new BigDecimal(3.0)));
            put("PV Privatissimum aus Fachdidaktik Informatik", new Subject("PV Privatissimum aus Fachdidaktik Informatik", new BigDecimal(4.0)));
            put("VU Präsentation und Moderation", new Subject("VU Präsentation und Moderation", new BigDecimal(3.0)));
        }
    };

    private HashMap<String, Subject> subjectsFreeChoiceInformatics = new HashMap<String, Subject>() {{
        put("VU Pilots in Mobile Interaction: User-centered Interaction Research and Evaluation", new Subject("VU Pilots in Mobile Interaction: User-centered Interaction Research and Evaluation", new BigDecimal(3.0)));
        put("VU Robotik für RoboCup", new Subject("VU Robotik für RoboCup", new BigDecimal(6.0)));
        put("KO Reflections on ICTs and Society", new Subject("KO Reflections on ICTs and Society", new BigDecimal(6.0)));
        put("VU Science of Information 1: Transdisciplinary Foundations of Informatics", new Subject("VU Science of Information 1: Transdisciplinary Foundations of Informatics", new BigDecimal(3.0)));
        put("VU Parallel Computing", new Subject("VU Parallel Computing", new BigDecimal(6.0)));
        put("PR Praktikum aus IT-Security", new Subject("PR Praktikum aus IT-Security", new BigDecimal(6.0)));
        put("SE Formale Semantik natürlicher Sprache", new Subject("SE Formale Semantik natürlicher Sprache", new BigDecimal(3.0)));
        put("VO Ausgewählte Kapitel der Technischen Informatik", new Subject("VO Ausgewählte Kapitel der Technischen Informatik", new BigDecimal(3.0)));
        put("SE Seminar für DiplomandInnen", new Subject("SE Seminar für DiplomandInnen", new BigDecimal(2.0)));
        put("SE Computer Vision Seminar für DiplomandInnen", new Subject("SE Computer Vision Seminar für DiplomandInnen", new BigDecimal(2.0)));
        put("VU Softwaretechische Konzepte und Infrastrukturtechnologien zur Identifikation von Objekten und Geräten: RFID, Smartcards, NFC und Mobile Phones", new Subject("VU Softwaretechische Konzepte und Infrastrukturtechnologien zur Identifikation von Objekten und Geräten: RFID, Smartcards, NFC und Mobile Phones", new BigDecimal(3.0)));
        put("VU Advanced Services Engineering", new Subject("VU Advanced Services Engineering", new BigDecimal(3.0)));
        put("SE Forschungsseminar für DiplomandInnen", new Subject("SE Forschungsseminar für DiplomandInnen", new BigDecimal(3.0)));
        put("VO Finanzmärkte, Finanzintermediation und Kapitalanlage", new Subject("VO Finanzmärkte, Finanzintermediation und Kapitalanlage", new BigDecimal(3.5)));
        put("VU Methodisches, industrielles Software-Engineering mit Funktionalen Sprachen am Fallbeispiel Haskell", new Subject("VU Methodisches, industrielles Software-Engineering mit Funktionalen Sprachen am Fallbeispiel Haskell", new BigDecimal(3.0)));
        put("VU IT Governance", new Subject("VU IT Governance", new BigDecimal(3.0)));
        put("VU Current Trends in Computer Science", new Subject("VU Current Trends in Computer Science", new BigDecimal(3.0)));
        put("VU Programmierung von Strategie-Spielen", new Subject("VU Programmierung von Strategie-Spielen", new BigDecimal(3.0)));
        put("VU Medienanalyse und Medienreflexion", new Subject("VU Medienanalyse und Medienreflexion", new BigDecimal(3.0)));
        put("EX Exkursion", new Subject("EX Exkursion", new BigDecimal(3.0)));
        put("VU Privacy Enhancing Technologies", new Subject("VU Privacy Enhancing Technologies", new BigDecimal(3.0)));
        put("VU Theoretische Informatik und Logik für CI", new Subject("VU Theoretische Informatik und Logik für CI", new BigDecimal(6.0)));
        put("SE Theorie und Praxis der Evaluierung von innovativen User Interfaces", new Subject("SE Theorie und Praxis der Evaluierung von innovativen User Interfaces", new BigDecimal(3.0)));
        put("VU Brückenkurs Programmierung für Studienanfängerinnen", new Subject("VU Brückenkurs Programmierung für Studienanfängerinnen", new BigDecimal(3.0)));
        put("VU Mobile (App) Software Engineering", new Subject("VU Mobile (App) Software Engineering", new BigDecimal(3.0)));
        put("VU Runtime Verification", new Subject("VU Runtime Verification", new BigDecimal(3.0)));
        put("VU 2D and 3D Image Registration", new Subject("VU 2D and 3D Image Registration", new BigDecimal(3.0)));
        put("VU Mobile Robotik", new Subject("VU Mobile Robotik", new BigDecimal(4.5)));
        put("SE Critical Algorithm Studies", new Subject("SE Critical Algorithm Studies", new BigDecimal(3.0)));
        put("VU Brückenkurs Programmierung für Studienanfänger", new Subject("", new BigDecimal(0.0)));
    }};

    private List<Subject> subjectsBachelorSoftwareAndInformationEngineering;

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
        createSubjectsBachelorSoftwareAndInformationEngineering();
        Iterable<Subject> subjects = subjectRepository.save(subjectsBachelorSoftwareAndInformationEngineering);

        this.subjects = StreamSupport.stream(subjects.spliterator(), false).collect(Collectors.toList());
    }

    private void createSubjectsBachelorSoftwareAndInformationEngineering() {
        subjectsBachelorSoftwareAndInformationEngineering = new ArrayList<>(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester1.values());
        subjectsBachelorSoftwareAndInformationEngineering.addAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester2.values());
        subjectsBachelorSoftwareAndInformationEngineering.addAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester3.values());
        subjectsBachelorSoftwareAndInformationEngineering.addAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester4.values());
        subjectsBachelorSoftwareAndInformationEngineering.addAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester5.values());
        subjectsBachelorSoftwareAndInformationEngineering.addAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester6.values());
        subjectsBachelorSoftwareAndInformationEngineering.addAll(subjectsOptionalBachelorSoftwareAndInformationEngineering.values());
        subjectsBachelorSoftwareAndInformationEngineering.addAll(subjectsFreeChoiceInformatics.values());
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
                new StudyPlan("Master Computational Intelligence", new EctsDistribution(new BigDecimal(60), new BigDecimal(30), new BigDecimal(30))),
                new StudyPlan("Master Visual Computing", new EctsDistribution(new BigDecimal(60), new BigDecimal(30), new BigDecimal(30))),
                new StudyPlan("Master Medical Informatics", new EctsDistribution(new BigDecimal(60), new BigDecimal(30), new BigDecimal(30))),
                new StudyPlan("Master Computer Science", new EctsDistribution(new BigDecimal(60), new BigDecimal(30), new BigDecimal(30)))
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
        addBachelorSoftwareAndInformationEngineeringSubjectsToStudyPlan();
    }

    private void addBachelorSoftwareAndInformationEngineeringSubjectsToStudyPlan() {
        for (String subjectName : subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester1.keySet()) {
            subjectForStudyPlanRepository.save(new SubjectForStudyPlan(
                    subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester1.get(subjectName), studyplans.get(0), true, 1)
            );
        }

        for (String subjectName : subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester2.keySet()) {
            subjectForStudyPlanRepository.save(new SubjectForStudyPlan(
                    subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester2.get(subjectName), studyplans.get(0), true, 2)
            );
        }

        for (String subjectName : subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester3.keySet()) {
            subjectForStudyPlanRepository.save(new SubjectForStudyPlan(
                    subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester3.get(subjectName), studyplans.get(0), true, 3)
            );
        }

        for (String subjectName : subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester4.keySet()) {
            subjectForStudyPlanRepository.save(new SubjectForStudyPlan(
                    subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester4.get(subjectName), studyplans.get(0), true, 4)
            );
        }

        for (String subjectName : subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester5.keySet()) {
            subjectForStudyPlanRepository.save(new SubjectForStudyPlan(
                    subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester5.get(subjectName), studyplans.get(0), true, 5)
            );
        }

        for (String subjectName : subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester6.keySet()) {
            subjectForStudyPlanRepository.save(new SubjectForStudyPlan(
                    subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester6.get(subjectName), studyplans.get(0), true, 6)
            );
        }

        for (String subjectName : subjectsOptionalBachelorSoftwareAndInformationEngineering.keySet()) {
            subjectForStudyPlanRepository.save(new SubjectForStudyPlan(
                    subjectsOptionalBachelorSoftwareAndInformationEngineering.get(subjectName), studyplans.get(0), false)
            );
        }
    }

}
