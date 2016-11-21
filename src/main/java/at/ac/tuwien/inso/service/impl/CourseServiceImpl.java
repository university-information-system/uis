package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import javax.validation.constraints.*;
import java.util.*;
import java.util.stream.*;

@Service
public class CourseServiceImpl implements CourseService {

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

    @Override
    @Transactional(readOnly = true)
    public List<Course> findCourseForCurrentSemesterWithName(@NotNull String name) {
        Semester semester = semesterService.getCurrentSemester();
        return courseRepository.findAllBySemesterAndSubjectNameLikeIgnoreCase(semester, "%" + name + "%");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findCoursesForCurrentSemesterForLecturer(Lecturer lecturer) {
        Semester semester = semesterService.getCurrentSemester();
        Iterable<Subject> subjectsForLecturer = subjectRepository.findByLecturers_Id(lecturer.getId());
        List<Course> courses = new ArrayList<>();
        subjectsForLecturer.forEach(subject -> courses.addAll(courseRepository.findAllBySemesterAndSubject(semester, subject)));
        return courses;
    }

    @Override
    @Transactional
    public Course saveCourse(AddCourseForm form) {
        Course course = form.getCourse();
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
        return courseRepository.findOne(id);
    }


    @Override
    @Transactional
    public boolean registerStudentForCourse(Course course) {
        Student student = studentRepository.findOne(userAccountService.getCurrentLoggedInUser().getId());
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

}
