package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.dto.GradeAuthorizationDTO;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;

import org.jboss.aerogear.security.otp.Totp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class GradeServiceImpl implements GradeService {
	
	private static final Logger log = LoggerFactory.getLogger(GradeServiceImpl.class);

    private GradeRepository gradeRepository;
    private StudentService studentService;
    private CourseService courseService;
    private LecturerService lecturerService;
    private UserAccountService userAccountService;

    @Autowired
    public GradeServiceImpl(GradeRepository gradeRepository,
                            StudentService studentService,
                            CourseService courseService,
                            LecturerService lecturerService,
                            UserAccountService userAccountService) {
        this.gradeRepository = gradeRepository;
        this.studentService = studentService;
        this.courseService = courseService;
        this.lecturerService = lecturerService;
        this.userAccountService = userAccountService;
    }

    @Override
    public GradeAuthorizationDTO getDefaultGradeAuthorizationDTOForStudentAndCourse(Long studentId, Long courseId) {
        Student student = studentService.findOne(studentId);
        Lecturer lecturer = lecturerService.getLoggedInLecturer();
        Course course = courseService.findOne(courseId);
        if (course == null || lecturer == null || student == null) {
            throw new BusinessObjectNotFoundException("Wrong student or course id");
        }
        if (!course.getStudents().contains(student)) {
            throw new ValidationException("Student not registered for course!");
        }
        return new GradeAuthorizationDTO(new Grade(course, lecturer, student, Mark.FAILED));
    }

    @Override
    public Grade saveNewGradeForStudentAndCourse(GradeAuthorizationDTO gradeAuthorizationDTO) {
        Grade grade = gradeAuthorizationDTO.getGrade();
        if (!grade.getLecturer().equals(lecturerService.getLoggedInLecturer())) {
            throw new ValidationException("Lecturer is not valid!");
        }
        String oneTimePassword = gradeAuthorizationDTO.getAuthCode();
        Totp authenticator = new Totp(grade.getLecturer().getTwoFactorSecret());
        if(!authenticator.verify(oneTimePassword)) {
            throw new ValidationException("Auth-code is not valid!");
        }
        return gradeRepository.save(grade);
    }

    @Override
    public List<Grade> getGradesForCourseOfLoggedInLecturer(Long courseId) {
        Lecturer lecturer = lecturerService.getLoggedInLecturer();
        log.info("getting grades for couse of logged in lecturer with courseid" +courseId +" and lecturerid "+lecturer.getId());
        return gradeRepository.findByLecturerIdAndCourseId(lecturer.getId(), courseId);
    }

    @Override
    public List<Grade> getGradesForLoggedInStudent() {
        Long studentId =  userAccountService.getCurrentLoggedInUser().getId();
        log.info("getting grades for logged in student with id " + studentId);
        return gradeRepository.findByStudentAccountId(studentId);
    }

    @Override
    public Grade getForValidation(String identifier) {
    	log.info("getting validation for identifier "+identifier);
        Long gradeId = parseValidationIdentifier(identifier);
        return gradeRepository.findOne(gradeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grade> findAllOfStudent(Student student) {
    	log.info("finding all grades for student with id " + student.getId());
        return gradeRepository.findAllOfStudent(student);
    }

    @Override
    public List<Mark> getMarkOptions() {
        return Arrays.asList(Mark.EXCELLENT, Mark.GOOD, Mark.SATISFACTORY, Mark.SUFFICIENT, Mark.FAILED);
    }

    private Long parseValidationIdentifier(String identifier) {
        return Long.valueOf(identifier);
    }
}
