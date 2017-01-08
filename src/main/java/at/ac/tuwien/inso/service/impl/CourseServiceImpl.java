package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.dto.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import javax.validation.constraints.*;
import java.util.*;
import java.util.stream.*;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Course> findCourseForCurrentSemesterWithName(@NotNull String name) {
    	log.info("try to find course for current semester with semestername: "+name);
        Semester semester = semesterService.getCurrentSemester().toEntity();
        return courseRepository.findAllBySemesterAndSubjectNameLikeIgnoreCase(semester, "%" + name + "%");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findCoursesForCurrentSemesterForLecturer(Lecturer lecturer) {
    	log.info("try finding courses for current semester for lecturer with id "+lecturer.getId());
        Semester semester = semesterService.getCurrentSemester().toEntity();
        Iterable<Subject> subjectsForLecturer = subjectRepository.findByLecturers_Id(lecturer.getId());
        List<Course> courses = new ArrayList<>();
        subjectsForLecturer.forEach(subject -> courses.addAll(courseRepository.findAllBySemesterAndSubject(semester, subject)));
        return courses;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Course> findCoursesForSubject(Subject subject) {    
    	log.info("try finding course for subject with id "+subject.getId());
    	return courseRepository.findAllBySubject(subject);    	
    	
    }

    @Override
    @Transactional
    public Course saveCourse(AddCourseForm form) {
    	log.info("try saving course");
        Course course = form.getCourse();
        log.info("try saving course "+course.toString());
        List<Tag> tags = form.getActiveAndInactiveTags().stream()
                .filter(tagBooleanEntry -> tagBooleanEntry.isActive())
                .map(tagBooleanEntry -> tagBooleanEntry.getTag())
                .collect(Collectors.toList());
        tags.forEach(tag -> course.addTags(tag));
        if (!(course.getStudentLimits() > 0)) {
            course.setStudentLimits(1);
        }
        return courseRepository.save(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Course findOne(Long id) {
    	log.info("try finding course with id "+id);
        Course course = courseRepository.findOne(id);
        if (course == null) {
        	log.warn("Course with id " + id + " does not exist");
            throw new BusinessObjectNotFoundException("Course with id " + id + " does not exist");
        }
        return course;
    }


    @Override
    @Transactional
    public boolean registerStudentForCourse(Course course) {
    	Student student = studentRepository.findByUsername(userAccountService.getCurrentLoggedInUser().getUsername());
    	if(student!=null){
    		log.info("try registering currently logged in student with id "+student.getId()+" for course with id "+course.getId());
    	}else{
    		log.info("try registering currently logged in student with id null for course with id "+course.getId());
    	}
        if (course.getStudentLimits() <= course.getStudents().size()) {
            return false;
        } else if (course.getStudents().contains(student)) {
            return true;
        } else {
            course.addStudents(student);
            courseRepository.save(course);
            return true;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAllForStudent(Student student) {
        log.info("finding all courses for student with id "+student.getId());
    	return courseRepository.findAllForStudent(student);
    }

    @Override
    @Transactional
    public void unregisterStudentFromCourse(Student student, Long courseId) {
        log.info("Unregistering student with id {} from course with id {}", student.getId(), courseId);

        Course course = courseRepository.findOne(courseId);
        if (course == null) {
            log.warn("Course with id {} not found. Nothing to unregister", courseId);
            return;
        }

        course.removeStudents(student);
    }

    @Override
    public CourseDetailsForStudent courseDetailsFor(Student student, Long courseId) {
    	log.info("reading course details for student with id "+student.getId()+" from course with id "+ courseId);

    	Course course = findOne(courseId);

        return new CourseDetailsForStudent(course)
                .setCanEnroll(canEnrollToCourse(student, course))
                .setStudyplans(subjectForStudyPlanRepository.findBySubject(course.getSubject()));
    }

    private boolean canEnrollToCourse(Student student, Course course) {
        return course.getSemester().toDto().equals(semesterService.getCurrentSemester()) &&
                !courseRepository.existsCourseRegistration(student, course);
    }
}
