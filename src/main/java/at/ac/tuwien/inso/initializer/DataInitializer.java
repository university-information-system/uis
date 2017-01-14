package at.ac.tuwien.inso.initializer;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.student_subject_prefs.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.math.*;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.*;

import static java.util.Arrays.*;

@Component
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
    @Autowired
    private PendingAccountActivationRepository pendingAccountActivationRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private StudentSubjectPreferenceStore studentSubjectPreferenceStore;

    private List<StudyPlan> studyplans;

    private List<Semester> semesters;

    private List<Subject> subjects;

    private List<Course> courses;

    private List<Student> students;

    private List<Lecturer> lecturers;

    private Map<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester1 = new HashMap<String, Subject>() {
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

    private Map<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester2 = new HashMap<String, Subject>() {
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

    private Map<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester3 = new HashMap<String, Subject>() {
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

    private Map<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester4 = new HashMap<String, Subject>() {
        {
            put("VU Einführung in die Künstliche Intelligenz", new Subject("VU Einführung in die Künstliche Intelligenz", new BigDecimal(3.0)));
            put("VU Theoretische Informatik und Logik", new Subject("VU Theoretische Informatik und Logik", new BigDecimal(6.0)));
            put("VO Software Engineering und Projektmanagement", new Subject("VO Software Engineering und Projektmanagement", new BigDecimal(3.0)));
            put("PR Software Engineering und Projektmanagement", new Subject("PR Software Engineering und Projektmanagement", new BigDecimal(6.0)));
        }
    };

    private Map<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester5 = new HashMap<String, Subject>() {
        {
            put("VO Verteilte Systeme", new Subject("VO Verteilte Systeme", new BigDecimal(3.0)));
            put("UE Verteilte Systeme", new Subject("UE Verteilte Systeme", new BigDecimal(3.0)));
            put("VU Gesellschaftswissenschaftliche Grundlagen der Informatik", new Subject("VU Gesellschaftswissenschaftliche Grundlagen der Informatik", new BigDecimal(3.0)));
            put("VU Interface and Interaction Design", new Subject("VU Interface and Interaction Design", new BigDecimal(3.0)));
            put("VU Einführung in wissensbasierte Systeme", new Subject("VU Einführung in wissensbasierte Systeme", new BigDecimal(3.0)));
            put("SE Wissenschaftliches Arbeiten", new Subject("SE Wissenschaftliches Arbeiten", new BigDecimal(3.0)));
        }
    };

    private Map<String, Subject> subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester6 = new HashMap<String, Subject>() {
        {
            put("PR Bachelorarbeit für Informatik und Wirtschaftsinformatik", new Subject("PR Bachelorarbeit für Informatik und Wirtschaftsinformatik", new BigDecimal(10.0)));
        }
    };

    private Map<String, Subject> subjectsOptionalBachelorSoftwareAndInformationEngineering = new HashMap<String, Subject>() {
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

    private Map<String, Subject> subjectsFreeChoiceInformatics = new HashMap<String, Subject>() {{
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

    private Map<String, Subject> subjectsBachelorSoftwareAndInformationEngineering;

    private Map<String, Course> coursesBachelorSoftwareAndInformationEngineering;

    private Map<String, Tag> tags = new HashMap<String, Tag>() {{
        put("Programmieren", new Tag("Programmieren"));
        put("Java", new Tag("Java"));
        put("Debug", new Tag("Debug"));
        put("Rekursion", new Tag("Rekursion"));
        put("Boolsche Algebra", new Tag("Boolsche Algebra"));
        put("RISC", new Tag("RISC"));
        put("CISC", new Tag("CISC"));
        put("Pipelining", new Tag("Pipelining"));
        put("ROM", new Tag("ROM"));
        put("PROM/EPROM", new Tag("PROM/EPROM"));
        put("2 Complement", new Tag("2 Complement"));
        put("1 Complement", new Tag("1 Complement"));
        put("Gatterschaltungen", new Tag("Gatterschaltungen"));
        put("Befehlssatz", new Tag("Befehlssatz"));
        put("Digital", new Tag("Digital"));
        put("Zahlentheorie", new Tag("Zahlentheorie"));
        put("Aussagenlogik", new Tag("Aussagenlogik"));
        put("Mengenlehre", new Tag("Mengenlehre"));
        put("Kombinatorik,", new Tag("Kombinatorik,"));
        put("Differenzengleichungen", new Tag("Differenzengleichungen"));
        put("Graphentheorie", new Tag("Graphentheorie"));
        put("Algebraische Strukturen", new Tag("Algebraische Strukturen"));
        put("Lineare Algebra", new Tag("Lineare Algebra"));
        put("Codierungstheorie", new Tag("Codierungstheorie"));
        put("UE", new Tag("UE"));
        put("VU", new Tag("VU"));
        put("VO", new Tag("VO"));
        put("Automaten", new Tag("Automaten"));
        put("reguläre Ausdrücke", new Tag("reguläre Ausdrücke"));
        put("Grammatiken", new Tag("Grammatiken"));
        put("Petri-Netze", new Tag("Petri-Netze"));
        put("Prädikatenlogik", new Tag("Prädikatenlogik"));
        put("EER", new Tag("EER"));
        put("Relationenmodel", new Tag("Relationenmodel"));
        put("Domänenkalkül", new Tag("Domänenkalkül"));
        put("Datenbanksprachen", new Tag("Datenbanksprachen"));
        put("Relationale Entwurfstheorie", new Tag("Relationale Entwurfstheorie"));
        put("Normalformen", new Tag("Normalformen"));
        put("Datenintegrität", new Tag("Datenintegrität"));
        put("SQL", new Tag("SQL"));
        put("JDBC", new Tag("JDBC"));
        put("DBMS", new Tag("DBMS"));
        put("Algorithmen", new Tag("Algorithmen"));
        put("Datenstrukturen", new Tag("Datenstrukturen"));
        put("Visual Computing", new Tag("Visual Computing"));
        put("MATLAB", new Tag("MATLAB"));
        put("Fourier", new Tag("Fourier"));
        put("Analysis", new Tag("Analysis"));
        put("Langweilig", new Tag("Langweilig"));
        put("Theorie", new Tag("Theorie"));
        put("Gesellschaft", new Tag("Gesellschaft"));
        put("Human Computer Interaction", new Tag("Human Computer Interaction"));
        put("Laplace", new Tag("Laplace"));
        put("Laplace", new Tag("Laplace"));
        put("Folgen", new Tag("Folgen"));
        put("Reihen", new Tag("Reihen"));
        put("Stetigkeit", new Tag("Stetigkeit"));
        put("Grenzwerte", new Tag("Grenzwerte"));
        put("Nullstellen", new Tag("Nullstellen"));
        put("Differentialrechnung", new Tag("Differentialrechnung"));
        put("Funktionen", new Tag("Funktionen"));
        put("Integralrechnung", new Tag("Integralrechnung"));
        put("Objektorientiert", new Tag("Objektorientiert"));
        put("UML", new Tag("UML"));
        put("Grundlagen", new Tag("Grundlagen"));
        put("Hardware", new Tag("Hardware"));
        put("Software", new Tag("Software"));
        put("Computer Science", new Tag("Computer Science"));
        put("Mathe", new Tag("Mathe"));
        put("Spaß", new Tag("Spaß"));
        put("Einfach", new Tag("Einfach"));
        put("Diffizil", new Tag("Diffizil"));
    }};

    private Map<String, Student> studentMap = new HashMap<String, Student>() {
        {
            put("Caroline Black", new Student("s1123960", "Caroline Black", "caroline.black@uis.at", new UserAccount("student", "pass", Role.STUDENT)));
            put("Emma Dowd", new Student("s1127157", "Emma Dowd", "emma.dowd@gmail.com", new UserAccount("emma", "pass", Role.STUDENT)));
            put("John Terry", new Student("s1126441", "John Terry", "john.terry@uis.at", new UserAccount("john", "pass", Role.STUDENT)));
            put("Joan Watson", new Student("s0227157", "Joan Watson", "joan.watson@uit.at"));
            put("James Bond", new Student("s1527199", "James Bond", "jamesbond_007@yahoo.com"));
            put("Trevor Bond", new Student("s0445157", "Trevor Bond", "trevor@uis.at"));
        }
    };

    private Map<String, Lecturer> lecturerMap = new HashMap<String, Lecturer>() {
        {
            put("Carol Sanderson", new Lecturer("l0100010", "Carol Sanderson", "carol@uis.at"));
            put("Una Walker", new Lecturer("l0100011", "Una Walker", "una.walker@uis.at", new UserAccount("lecturer", "pass", Role.LECTURER)));
            put("Connor MacLeod", new Lecturer("l0203019", "Connor MacLeod", "connor@gmail.com"));
            put("Eric Wilkins", new Lecturer("l1100010", "Eric Wilkins", "e1234567@tuwien.ac.at", new UserAccount("eric", "pass", Role.LECTURER)));
            put("Benjamin Piper", new Lecturer("l9123410", "Benjamin Piper", "ben@uis.at", new UserAccount("ben", "pass", Role.LECTURER)));
        }
    };

    @Transactional
    public void initialize() {
        userAccountRepository.save(new UserAccount("admin", "pass", Role.ADMIN));

        createTags();

        createSubjects();

        createSemesters();

        createCourses();

        createStudyPlans();

        createUsers();

        registerStudentsToStudyPlans();

        addPreconditionsToSubjects();

        registerSubjectsToLecturers();

        addTagsToCourses();

        addSubjectsToStudyPlans();

        registerCoursesToStudents();

        giveGrades();

        giveFeedback();
    }

    private void createTags() {
        tagRepository.save(tags.values());
    }

    private void createSubjects() {
        createSubjectsBachelorSoftwareAndInformationEngineering();
        Iterable<Subject> subjects = subjectRepository.save(subjectsBachelorSoftwareAndInformationEngineering.values());

        this.subjects = StreamSupport.stream(subjects.spliterator(), false).collect(Collectors.toList());
    }

    private void createSubjectsBachelorSoftwareAndInformationEngineering() {
        subjectsBachelorSoftwareAndInformationEngineering = new HashMap<>();
        subjectsBachelorSoftwareAndInformationEngineering.putAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester1);
        subjectsBachelorSoftwareAndInformationEngineering.putAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester2);
        subjectsBachelorSoftwareAndInformationEngineering.putAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester3);
        subjectsBachelorSoftwareAndInformationEngineering.putAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester4);
        subjectsBachelorSoftwareAndInformationEngineering.putAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester5);
        subjectsBachelorSoftwareAndInformationEngineering.putAll(subjectsMandatoryBachelorSoftwareAndInformationEngineeringSemester6);
        subjectsBachelorSoftwareAndInformationEngineering.putAll(subjectsOptionalBachelorSoftwareAndInformationEngineering);
        subjectsBachelorSoftwareAndInformationEngineering.putAll(subjectsFreeChoiceInformatics);
    }

    private void createSemesters() {
        Iterable<Semester> semesters = semesterRepository.save(asList(
                new Semester(2016, SemesterType.SummerSemester),
                new Semester(2016, SemesterType.WinterSemester)
        ));

        this.semesters = StreamSupport.stream(semesters.spliterator(), false).collect(Collectors.toList());
    }

    private void createCourses() {
        createCoursesBachelorSoftwareAndInformationEngineering();
        Iterable<Course> courses = courseRepository.save(coursesBachelorSoftwareAndInformationEngineering.values());

        this.courses = StreamSupport.stream(courses.spliterator(), false).collect(Collectors.toList());
    }

    private void createCoursesBachelorSoftwareAndInformationEngineering() {
        coursesBachelorSoftwareAndInformationEngineering = new HashMap<>();
        for (String subjectName : subjectsBachelorSoftwareAndInformationEngineering.keySet()) {
            coursesBachelorSoftwareAndInformationEngineering.put(
                    subjectName,
                    new Course(subjectsBachelorSoftwareAndInformationEngineering.get(subjectName), semesters.get(1)).setStudentLimits(20)
            );
        }
    }

    private void createStudyPlans() {
        Iterable<StudyPlan> studyplans = studyPlanRepository.save(asList(
                new StudyPlan("Bachelor Software and Information Engineering", new EctsDistribution(new BigDecimal(90), new BigDecimal(60), new BigDecimal(30))),
                new StudyPlan("Master Business Informatics", new EctsDistribution(new BigDecimal(30), new BigDecimal(70), new BigDecimal(20))),
                new StudyPlan("Master Computational Intelligence", new EctsDistribution(new BigDecimal(60), new BigDecimal(30), new BigDecimal(30))),
                new StudyPlan("Master Visual Computing", new EctsDistribution(new BigDecimal(60), new BigDecimal(30), new BigDecimal(30))),
                new StudyPlan("Master Medical Informatics", new EctsDistribution(new BigDecimal(60), new BigDecimal(30), new BigDecimal(30)), false),
                new StudyPlan("Master Computer Science", new EctsDistribution(new BigDecimal(60), new BigDecimal(30), new BigDecimal(30)))
        ));

        this.studyplans = StreamSupport.stream(studyplans.spliterator(), false).collect(Collectors.toList());
    }

    private void createUsers() {
        pendingAccountActivationRepository.save(
                new PendingAccountActivation("test", new Student("test", "Test User", "test-user@uis.at"))
        );
        List<UisUser> usersList = new ArrayList<>(studentMap.values());
        usersList.addAll(lecturerMap.values());

        Iterable<UisUser> users = uisUserRepository.save(usersList);

        students = StreamSupport.stream(users.spliterator(), false)
                .filter(it -> it instanceof Student)
                .map(it -> (Student) it)
                .collect(Collectors.toList());

        lecturers = StreamSupport.stream(users.spliterator(), false)
                .filter(it -> it instanceof Lecturer)
                .map(it -> (Lecturer) it)
                .collect(Collectors.toList());
    }

    private void registerStudentsToStudyPlans() {
        studentMap.get("John Terry").addStudyplans(new StudyPlanRegistration(studyplans.get(0), semesters.get(1)));
        studentMap.get("Caroline Black").addStudyplans(new StudyPlanRegistration(studyplans.get(1), semesters.get(1)));
        studentMap.get("Emma Dowd").addStudyplans(new StudyPlanRegistration(studyplans.get(2), semesters.get(0)));
        studentMap.get("Joan Watson").addStudyplans(new StudyPlanRegistration(studyplans.get(3), semesters.get(1)));
        studentMap.get("James Bond").addStudyplans(new StudyPlanRegistration(studyplans.get(4), semesters.get(1)));
        studentMap.get("James Bond").addStudyplans(new StudyPlanRegistration(studyplans.get(5), semesters.get(0)));
        studentMap.get("Trevor Bond").addStudyplans(new StudyPlanRegistration(studyplans.get(1), semesters.get(1)));
    }

    private void addPreconditionsToSubjects() {
        subjects.get(2).addRequiredSubjects(subjects.get(1));
    }

    private void registerSubjectsToLecturers() {
        subjectsBachelorSoftwareAndInformationEngineering.get("UE Studieneingangsgespräch").addLecturers(
                lecturerMap.get("Carol Sanderson")
        );

        subjects.get(0).addLecturers(lecturers.get(3));
        subjects.get(1).addLecturers(lecturers.get(3));
        subjects.get(2).addLecturers(lecturers.get(3), lecturers.get(4));

        subjectRepository.save(subjects);

        Subject s = subjectsBachelorSoftwareAndInformationEngineering
                .get("VU Technische Grundlagen der Informatik");
        s.addLecturers(lecturers.get(3));
        subjectRepository.save(s);

        s = subjectsBachelorSoftwareAndInformationEngineering
                .get("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik");
        s.addLecturers(lecturers.get(3));
        subjectRepository.save(s);
    }


    private void addTagsToCourses() {
        addTagsToBachelorSoftwareAndInformationEngineeringCourses();
        courseRepository.save(coursesBachelorSoftwareAndInformationEngineering.values());
    }

    private void addTagsToBachelorSoftwareAndInformationEngineeringCourses() {
        addTagsToBachelorSoftwareAndInformationEngineeringCoursesSemester1();
        addTagsToBachelorSoftwareAndInformationEngineeringCoursesSemester2();
    }

    private void addTagsToBachelorSoftwareAndInformationEngineeringCoursesSemester1() {
        coursesBachelorSoftwareAndInformationEngineering.get("VU Programmkonstruktion").addTags(
                tags.get("VU"), tags.get("Programmieren"), tags.get("Java"), tags.get("Debug"),
                tags.get("Rekursion"), tags.get("Software"), tags.get("Einfach"), tags.get("Grundlagen"),
                tags.get("Objektorientiert")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VU Technische Grundlagen der Informatik").addTags(
                tags.get("VU"), tags.get("Boolsche Algebra"), tags.get("RISC"), tags.get("CISC"), tags.get("Pipelining"),
                tags.get("ROM"), tags.get("PROM/EPROM"), tags.get("2 Complement"), tags.get("1 Complement"),
                tags.get("Gatterschaltungen"), tags.get("Befehlssatz"), tags.get("Digital"), tags.get("Diffizil"), tags.get("Grundlagen"),
                tags.get("Hardware")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik").addTags(
                tags.get("VO"), tags.get("Zahlentheorie"), tags.get("Aussagenlogik"), tags.get("Mengenlehre"),
                tags.get("Mathe"), tags.get("Kombinatorik"), tags.get("Differenzengleichungen"),
                tags.get("Graphentheorie"), tags.get("Algebraische Strukturen"), tags.get("Lineare Algebra"),
                tags.get("Codierungstheorie"), tags.get("Einfach"), tags.get("Funktionen"), tags.get("Grundlagen")

        );

        coursesBachelorSoftwareAndInformationEngineering.get("UE Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik").addTags(
                tags.get("UE"), tags.get("Zahlentheorie"), tags.get("Aussagenlogik"), tags.get("Mengenlehre"),
                tags.get("Mathe"), tags.get("Kombinatorik"), tags.get("Differenzengleichungen"),
                tags.get("Graphentheorie"), tags.get("Algebraische Strukturen"), tags.get("Lineare Algebra"),
                tags.get("Codierungstheorie"), tags.get("Einfach"), tags.get("Funktionen"), tags.get("Grundlagen")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VU Formale Modellierung").addTags(
                tags.get("VU"), tags.get("Automaten"), tags.get("reguläre Ausdrücke"), tags.get("formale Grammatiken"),
                tags.get("Aussagenlogik"), tags.get("Petri-Netze"), tags.get("Prädikatenlogik"), tags.get("Diffizil"), tags.get("Grundlagen")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VU Datenmodellierung").addTags(
                tags.get("VU"), tags.get("EER"), tags.get("Relationenmodel"), tags.get("Domänenkalkül"),
                tags.get("Datenbanksprachen"), tags.get("Normalformen"), tags.get("Relationale Entwurfstheorie"),
                tags.get("Datenintegrität"), tags.get("Einfach"), tags.get("SQL"), tags.get("Grundlagen")
        );
    }

    private void addTagsToBachelorSoftwareAndInformationEngineeringCoursesSemester2() {
        coursesBachelorSoftwareAndInformationEngineering.get("VU Datenbanksysteme").addTags(
                tags.get("VU"), tags.get("EER"), tags.get("Relationenmodel"), tags.get("Domänenkalkül"),
                tags.get("Datenbanksprachen"), tags.get("Normalformen"), tags.get("SQL"),
                tags.get("Datenintegrität"), tags.get("Diffizil"), tags.get("JDBC"), tags.get("DBMS")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VU Algorithmen und Datenstrukturen 1").addTags(
                tags.get("VU"), tags.get("Datenstrukturen"), tags.get("Algorithmen"), tags.get("Java"),
                tags.get("Programmieren"), tags.get("Debug"), tags.get("Rekursion"),
                tags.get("Software"), tags.get("Graphentheorie"), tags.get("Einfach")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VU Algorithmen und Datenstrukturen 2").addTags(
                tags.get("VU"), tags.get("Datenstrukturen"), tags.get("Algorithmen"), tags.get("Java"),
                tags.get("Programmieren"), tags.get("Debug"), tags.get("Rekursion"),
                tags.get("Software"), tags.get("Graphentheorie"), tags.get("Diffizil")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VU Einführung in Visual Computing").addTags(
                tags.get("VU"), tags.get("Visual Computing"), tags.get("MATLAB"), tags.get("Mathe"),
                tags.get("Fourier"), tags.get("Analysis"), tags.get("Diffizil"), tags.get("Programmieren"),
                tags.get("Grundlagen")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VU Gesellschaftliche Spannungsfelder der Informatik").addTags(
                tags.get("VU"), tags.get("Langweilig"), tags.get("Theorie"), tags.get("Einfach"),
                tags.get("Gesellschaft")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VU Basics of Human Computer Interaction").addTags(
                tags.get("VU"), tags.get("Human Computer Interaction"), tags.get("Einfach"), tags.get("Grundlagen")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VO Analysis für Informatik und Wirtschaftsinformatik").addTags(
                tags.get("VU"), tags.get("Mathe"), tags.get("Analysis"), tags.get("Fourier"),
                tags.get("Laplace"), tags.get("Diffizil"), tags.get("Folgen"),
                tags.get("Reihen"), tags.get("Stetigkeit"), tags.get("Grenzwerte"),
                tags.get("Nullstellen"), tags.get("Differentialrechnung"),
                tags.get("Integralrechnung"), tags.get("Funktionen")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("UE Analysis für Informatik und Wirtschaftsinformatik").addTags(
                tags.get("UE"), tags.get("Mathe"), tags.get("Analysis"), tags.get("Fourier"),
                tags.get("Laplace"), tags.get("Diffizil"), tags.get("Folgen"),
                tags.get("Reihen"), tags.get("Stetigkeit"), tags.get("Grenzwerte"),
                tags.get("Nullstellen"), tags.get("Differentialrechnung"),
                tags.get("Integralrechnung"), tags.get("Funktionen")
        );

        coursesBachelorSoftwareAndInformationEngineering.get("VU Objektorientierte Modellierung").addTags(
                tags.get("VU"), tags.get("Objektorientiert"), tags.get("UML"), tags.get("Grundlagen"),
                tags.get("Einfach")
        );
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

    private void registerCoursesToStudents() {
        // Joan Watson
        register(studentMap.get("Joan Watson"), coursesBachelorSoftwareAndInformationEngineering.get("UE Studieneingangsgespräch"));
        register(studentMap.get("Joan Watson"), coursesBachelorSoftwareAndInformationEngineering.get("VU Technische Grundlagen der Informatik"));
        register(studentMap.get("Joan Watson"), coursesBachelorSoftwareAndInformationEngineering.get("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik"));

        // Emma Dowd
        register(studentMap.get("Emma Dowd"), coursesBachelorSoftwareAndInformationEngineering.get("VU Technische Grundlagen der Informatik"));
        register(studentMap.get("Emma Dowd"), coursesBachelorSoftwareAndInformationEngineering.get("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik"));

        // Caroline Black
        register(studentMap.get("Caroline Black"), coursesBachelorSoftwareAndInformationEngineering.get("VU Technische Grundlagen der Informatik"));

        // John Terry
        Student john = studentMap.get("John Terry");
        register(john, coursesBachelorSoftwareAndInformationEngineering.get("VU Datenmodellierung"));
        register(john, coursesBachelorSoftwareAndInformationEngineering.get("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik"));
        register(john, coursesBachelorSoftwareAndInformationEngineering.get("VU Programmkonstruktion"));
    }

    private void register(Student student, Course course) {
        course.addStudents(student);
        studentSubjectPreferenceStore.studentRegisteredCourse(student, course);
    }

    private void giveGrades() {
        Course course = coursesBachelorSoftwareAndInformationEngineering.get("UE Studieneingangsgespräch");
        Student student = course.getStudents().get(0);
        Lecturer lecturer = course.getSubject().getLecturers().get(0);

        Grade grade = new Grade(course, lecturer, student, Mark.EXCELLENT);
        gradeRepository.save(grade);

        course = coursesBachelorSoftwareAndInformationEngineering.get("VU Technische Grundlagen der Informatik");
        student = studentMap.get("Emma Dowd");
        lecturer = course.getSubject().getLecturers().get(0);

        grade = new Grade(course, lecturer, student, Mark.EXCELLENT);
        gradeRepository.save(grade);

        course = coursesBachelorSoftwareAndInformationEngineering.get("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik");
        student = studentMap.get("Emma Dowd");
        lecturer = course.getSubject().getLecturers().get(0);

        grade = new Grade(course, lecturer, student, Mark.FAILED);
        gradeRepository.save(grade);

        grade = new Grade(
                coursesBachelorSoftwareAndInformationEngineering.get("VU Datenmodellierung"),
                lecturer,
                studentMap.get("John Terry"),
                Mark.EXCELLENT);
        gradeRepository.save(grade);

    }

    private void giveFeedback() {
        Course course = coursesBachelorSoftwareAndInformationEngineering.get("VU Datenmodellierung");
        Student johnTerry = studentMap.get("John Terry");
        Feedback feedback = new Feedback(johnTerry, course);

        course = coursesBachelorSoftwareAndInformationEngineering.get("VU Technische Grundlagen der Informatik");
        Student joanWatson =  studentMap.get("Joan Watson");
        Student emmaDowd = studentMap.get("Emma Dowd");
        Student carolineBlack = studentMap.get("Caroline Black");
        Feedback feedback1 = new Feedback(
                joanWatson,
                course,
                Feedback.Type.LIKE, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec enim ligula. " +
                "Sed eget posuere tellus. Aenean fermentum maximus tempor. Ut ultricies dapibus nulla vitae mollis. " +
                "Suspendisse a nunc nisi. Sed ut sapien eu odio sodales laoreet eu ac turpis. " +
                "In id sapien id ante sollicitudin consectetur at laoreet mi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Suspendisse quam sem, ornare eget pellentesque sit amet, tincidunt id metus. Sed scelerisque neque sed laoreet elementum. " +
                "Integer eros neque, vulputate a hendrerit at, ullamcorper in orci. Donec sit amet risus hendrerit, hendrerit magna non, dapibus nibh. " +
                "Suspendisse sed est feugiat, dapibus ante non, aliquet neque. Cras magna sapien, pharetra ut ante ut, malesuada hendrerit erat. " +
                "Mauris fringilla mattis dapibus. Nullam iaculis nunc in tortor gravida, id tempor justo elementum.");
        Feedback feedback2 = new Feedback(
                emmaDowd,
                course,
                Feedback.Type.DISLIKE, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec enim ligula. " +
                "Sed eget posuere tellus. Aenean fermentum maximus tempor. Ut ultricies dapibus nulla vitae mollis. " +
                "Suspendisse a nunc nisi. Sed ut sapien eu odio sodales laoreet eu ac turpis. " +
                "In id sapien id ante sollicitudin consectetur at laoreet mi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Suspendisse quam sem, ornare eget pellentesque sit amet, tincidunt id metus. Sed scelerisque neque sed laoreet elementum. " +
                "Integer eros neque, vulputate a hendrerit at, ullamcorper in orci. Donec sit amet risus hendrerit, hendrerit magna non, dapibus nibh. " +
                "Suspendisse sed est feugiat, dapibus ante non, aliquet neque. Cras magna sapien, pharetra ut ante ut, malesuada hendrerit erat. " +
                "Mauris fringilla mattis dapibus. Nullam iaculis nunc in tortor gravida, id tempor justo elementum.");
        Feedback feedback3 = new Feedback(
                carolineBlack,
                course,
                Feedback.Type.LIKE, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec enim ligula. " +
                "Sed eget posuere tellus. Aenean fermentum maximus tempor. Ut ultricies dapibus nulla vitae mollis. " +
                "Suspendisse a nunc nisi. Sed ut sapien eu odio sodales laoreet eu ac turpis. " +
                "In id sapien id ante sollicitudin consectetur at laoreet mi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Suspendisse quam sem, ornare eget pellentesque sit amet, tincidunt id metus. Sed scelerisque neque sed laoreet elementum. " +
                "Integer eros neque, vulputate a hendrerit at, ullamcorper in orci. Donec sit amet risus hendrerit, hendrerit magna non, dapibus nibh. " +
                "Suspendisse sed est feugiat, dapibus ante non, aliquet neque. Cras magna sapien, pharetra ut ante ut, malesuada hendrerit erat. " +
                "Mauris fringilla mattis dapibus. Nullam iaculis nunc in tortor gravida, id tempor justo elementum.");

        giveFeedback(feedback, feedback1, feedback2, feedback3);
    }

    private void giveFeedback(Feedback... feedbacks) {
        for (Feedback feedback : feedbacks) {
            feedbackRepository.save(feedback);
            studentSubjectPreferenceStore.studentGaveCourseFeedback(feedback.getStudent(), feedback);
        }
    }
}
