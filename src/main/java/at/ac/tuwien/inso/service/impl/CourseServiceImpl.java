package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.dto.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.student_subject_prefs.*;
import at.ac.tuwien.inso.validator.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.i18n.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import javax.validation.constraints.*;
import java.util.*;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);
    private ValidatorFactory validatorFactory = new ValidatorFactory();
    private CourseValidator validator = validatorFactory.getCourseValidator();

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
    private MessageSource messageSource;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    @Autowired
    private StudentSubjectPreferenceStore studentSubjectPreferenceStore;

    @Override
    @Transactional(readOnly = true)
    public Page<Course> findCourseForCurrentSemesterWithName(@NotNull String name, Pageable pageable) {
        log.info("try to find course for current semester with semestername: " + name + "and pageable " + pageable);
        Semester semester = semesterService.getOrCreateCurrentSemester().toEntity();
        return courseRepository.findAllBySemesterAndSubjectNameLikeIgnoreCase(semester, "%" + name + "%", pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findCoursesForCurrentSemesterForLecturer(Lecturer lecturer) {
        log.info("try finding courses for current semester for lecturer with id " + lecturer.getId());
        Semester semester = semesterService.getOrCreateCurrentSemester().toEntity();
        Iterable<Subject> subjectsForLecturer = subjectRepository.findByLecturers_Id(lecturer.getId());
        List<Course> courses = new ArrayList<>();
        subjectsForLecturer.forEach(subject -> courses.addAll(courseRepository.findAllBySemesterAndSubject(semester, subject)));
        return courses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findCoursesForSubject(Subject subject) {
        log.info("try finding course for subject with id " + subject.getId());
        return courseRepository.findAllBySubject(subject);
    }

    @Override
    public List<Course> findCoursesForSubjectAndCurrentSemester(Subject subject) {
        List<Course> result = courseRepository.findAllBySemesterAndSubject(semesterService.getCurrentSemester().toEntity(), subject);
        return result;
    }

    @Override
    @Transactional
    public void dismissCourse(Student student, Long courseId) {
        Course course = findOne(courseId);
        student.addDismissedCourse(course);
    }

    @Override
    @Transactional
    public Course saveCourse(AddCourseForm form) {
        log.info("try saving course");
        Course course = form.getCourse();
        validator.validateNewCourse(course);
        
        UserAccount u = userAccountService.getCurrentLoggedInUser();
        
        isLecturerAllowedToChangeCourse(course, u);
        
        log.info("try saving course " + course.toString());

        ArrayList<Tag> currentTagsOfCourse = new ArrayList<>(form.getCourse().getTags());

        for (String tag : form.getTags()) {
            Tag newTag = tagService.findByName(tag);

            // tag doesn't exist, so create a new one.
            if (newTag == null) {
                course.addTags(new Tag(tag));
            }
            // tag exists, but not in this course
            else if (!course.getTags().contains(newTag)) {
                course.addTags(newTag);
            }
            // tag already exists for this course
            else {
                currentTagsOfCourse.remove(newTag);
            }
        }

        // the remaining tags are to be removed
        course.removeTags(currentTagsOfCourse);

        if (!(course.getStudentLimits() > 0)) {
            course.setStudentLimits(1);
        }
        return courseRepository.save(course);
    }
    
    private void isLecturerAllowedToChangeCourse(Course c, UserAccount u){
    	if(c==null||u==null){
    		String msg = messageSource.getMessage("lecturer.course.edit.error.notallowed", null, LocaleContextHolder.getLocale());
    		throw new ValidationException(msg);
    	}
    	
    	if(u.hasRole(Role.ADMIN)){
    		log.info("user is admin, therefore course modification is allowed");
    		return;
    	}
    	
    	for(Lecturer l : c.getSubject().getLecturers()){
    		if(l.getAccount().equals(u)){
    			log.info("found equal lecturers, course modification is allowed");
    			return;
    		}
    	}
    	log.warn("suspisious try to modify course. user is not admin or does not own the subject for this course");
    	String msg = messageSource.getMessage("lecturer.course.edit.error.notallowed", null, LocaleContextHolder.getLocale());
    	throw new ValidationException(msg);
    }

    @Override
    @Transactional(readOnly = true)
    public Course findOne(Long id) {
        log.info("try finding course with id " + id);
        Course course = courseRepository.findOne(id);
        if (course == null) {
            log.warn("Course with id " + id + " does not exist");
            throw new BusinessObjectNotFoundException("Course with id " + id + " does not exist");
        }
        return course;
    }

    @Override
    @Transactional
    public boolean remove(Long courseId) throws ValidationException {
        log.info("try removing  course with id " + courseId);
        validator.validateCourseId(courseId); // throws ValidationException
        Course course = courseRepository.findOne(courseId);

        if (course == null) {
            String msg = "Course can not be deleted because there is no couse found with id " + courseId;
            log.warn(msg);
            throw new BusinessObjectNotFoundException(msg);
        }
        
        isLecturerAllowedToChangeCourse(course, userAccountService.getCurrentLoggedInUser());


        List<Grade> grades = gradeService.findAllByCourseId(courseId);

        if (grades != null && !grades.isEmpty()) {
            String msg = "There are grades for course [id:" + courseId + "], therefore it can not be removed.";
            log.warn(msg);
            throw new ValidationException(msg);
        }

        if (!course.getStudents().isEmpty()) {
            String msg = "There are students for course [id:" + courseId + "], therefore it can not be removed.";
            log.warn(msg);
            throw new ValidationException(msg);
        }

        log.info("successfully validated course removal. removing now!");
        courseRepository.delete(course);
        return true;
    }


    @Override
    @Transactional
    public boolean registerStudentForCourse(Course course) {
        validator.validateCourse(course);
        validator.validateCourseId(course.getId());
        Student student = studentRepository.findByUsername(userAccountService.getCurrentLoggedInUser().getUsername());

        log.info("try registering currently logged in student with id " + student.getId() + " for course with id " + course.getId());
        if (course.getStudentLimits() <= course.getStudents().size()) {
            return false;
        } else if (course.getStudents().contains(student)) {
            return true;
        } else {
            course.addStudents(student);
            courseRepository.save(course);
            studentSubjectPreferenceStore.studentRegisteredCourse(student, course);
            return true;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAllForStudent(Student student) {
        log.info("finding all courses for student with id " + student.getId());
        return courseRepository.findAllForStudent(student);
    }

    @Override
    @Transactional
    public Course unregisterStudentFromCourse(Student student, Long courseId) {
        log.info("Unregistering student with id {} from course with id {}", student.getId(), courseId);
        validator.validateCourseId(courseId);
        
        Course course = courseRepository.findOne(courseId);
        if (course == null) {
            log.warn("Course with id {} not found. Nothing to unregister", courseId);
            throw new BusinessObjectNotFoundException();
        }
        
        //students should only be able to unregister themselves
        if(userAccountService.getCurrentLoggedInUser().hasRole(Role.STUDENT)){
        	if(!student.getAccount().getUsername().equals(userAccountService.getCurrentLoggedInUser().getUsername())){
        		log.warn("student with id {} and username {} tried to unregister another one with id {} and username {}", userAccountService.getCurrentLoggedInUser().getId(), userAccountService.getCurrentLoggedInUser().getUsername(), student.getId(), student.getAccount().getUsername());
        		String msg = messageSource.getMessage("lecturer.course.edit.error.notallowed", null, LocaleContextHolder.getLocale());
        		throw new ValidationException(msg);
        	}
        }
        
        //Lectureres should only be able to remove students from their own courses
        if(userAccountService.getCurrentLoggedInUser().hasRole(Role.LECTURER)){
        	isLecturerAllowedToChangeCourse(course, userAccountService.getCurrentLoggedInUser());
        }
        

        course.removeStudents(student);
        studentSubjectPreferenceStore.studentUnregisteredCourse(student, course);
        return course;
    }


    @Override
    public CourseDetailsForStudent courseDetailsFor(Student student, Long courseId) {
        validator.validateCourseId(courseId);
        validator.validateStudent(student);
        log.info("reading course details for student with id " + student.getId() + " from course with id " + courseId);
        Course course = findOne(courseId);
        if (course == null) {
            log.warn("Course with id {} not found. Nothing to unregister", courseId);
            throw new BusinessObjectNotFoundException();
        }


        return new CourseDetailsForStudent(course).setCanEnroll(canEnrollToCourse(student, course)).setStudyplans(subjectForStudyPlanRepository.findBySubject(course.getSubject()));
    }

    @Override
    public List<SubjectForStudyPlan> getSubjectForStudyPlanList(Course course) {
        validator.validateCourse(course);
        return subjectForStudyPlanRepository.findBySubject(course.getSubject());
    }

    private boolean canEnrollToCourse(Student student, Course course) {
        validator.validateCourse(course);
        validator.validateStudent(student);
        return course.getSemester().toDto().equals(semesterService.getOrCreateCurrentSemester()) && !courseRepository.existsCourseRegistration(student, course);
    }

	@Override
	public List<Course> findAllCoursesForCurrentSemester() {
		SemesterDto semester = semesterService.getOrCreateCurrentSemester();
		return courseRepository.findAllBySemester(semester.toEntity());	
	}
}
