package at.ac.tuwien.inso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.GradeService;
import at.ac.tuwien.inso.service.LecturerService;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.UserAccountService;

@Service
public class GradeServiceImpl implements GradeService {

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
    public Grade getDefaultGradeForStudentAndCourse(Long studentId, Long courseId) {
        Student student = studentService.findOne(studentId);
        Lecturer lecturer = lecturerService.getLoggedInLecturer();
        Course course = courseService.findOne(courseId);
        if (course == null || lecturer == null || student == null) {
            throw new BusinessObjectNotFoundException("Wrong student or course id");
        }
        if (!course.getStudents().contains(student)) {
            throw new ValidationException("Student not registered for course!");
        }
        return new Grade(course, lecturer, student, BigDecimal.valueOf(5));
    }

    @Override
    public Grade saveNewGradeForStudentAndCourse(Grade grade) {
        if (!isMarkValid(grade)) {
            throw new ValidationException("Mark is not valid");
        }
        if (!grade.getLecturer().equals(lecturerService.getLoggedInLecturer())) {
            throw new ValidationException("Lecturer is not valid!");
        }
        return gradeRepository.save(grade);
    }

    @Override
    public List<Grade> getGradesForLoggedInStudent() {
        Long studentId =  userAccountService.getCurrentLoggedInUser().getId();
        return gradeRepository.findByStudentAccountId(studentId);
    }

    private boolean isMarkValid(Grade grade) {
        return (grade.getMark().compareTo(BigDecimal.ONE) > 0) &&
                (grade.getMark().compareTo(BigDecimal.valueOf(5)) < 0);
    }

    @Override
    public Grade getForValidation(String identifier) {
        Long gradeId = parseValidationIdentifier(identifier);
        return gradeRepository.findOne(gradeId);
    }

    private Long parseValidationIdentifier(String identifier) {
        return Long.valueOf(identifier);
    }
}
