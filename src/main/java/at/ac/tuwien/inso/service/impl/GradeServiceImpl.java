package at.ac.tuwien.inso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.service.GradeService;
import at.ac.tuwien.inso.service.LecturerService;
import at.ac.tuwien.inso.service.StudentService;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LecturerService lecturerService;

    @Override
    public Grade getDefaultGradeForStudentAndCourse(Long studentId, Long courseId) {
        Student student = studentService.findOne(studentId);
        Lecturer lecturer = lecturerService.getLoggedInLecturer();
        Course course = courseRepository.findOne(courseId);
        return new Grade(course, lecturer, student, BigDecimal.valueOf(5));
    }

    @Override
    public Grade saveNewGradeForStudentAndCourse(Grade grade) {
        return gradeRepository.save(grade);
    }
}
