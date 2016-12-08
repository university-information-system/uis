package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

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
        return new Grade(course, lecturer, student, Mark.FAILED);
    }

    @Override
    public Grade saveNewGradeForStudentAndCourse(Grade grade) {
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

    @Override
    public Grade getForValidation(String identifier) {
        Long gradeId = parseValidationIdentifier(identifier);
        return gradeRepository.findOne(gradeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grade> findAllOfStudent(Student student) {
        return gradeRepository.findAllOfStudent(student);
    }

    private Long parseValidationIdentifier(String identifier) {
        return Long.valueOf(identifier);
    }
}
