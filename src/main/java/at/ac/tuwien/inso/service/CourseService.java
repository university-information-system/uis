package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.controller.lecturer.forms.AddCourseForm;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

@Service
public class CourseService {

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


    @Transactional
    public List<Course> findCourseForCurrentSemesterWithName(@NotNull String name) {
        Semester semester = semesterService.getCurrentSemester();
        return courseRepository.findAllBySemesterAndSubjectNameLikeIgnoreCase(semester, "%" + name + "%");
    }

    @Transactional
    public List<Course> findCoursesForCurrentSemesterForLecturer(Lecturer lecturer) {
        Semester semester = semesterService.getCurrentSemester();
        Iterable<Subject> subjectsForLecturer = subjectRepository.findByLecturers_Id(lecturer.getId());
        List<Course> courses = new ArrayList<>();
        subjectsForLecturer.forEach(subject -> courses.addAll(courseRepository.findAllBySemesterAndSubject(semester, subject)));
        return courses;
    }

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

    @Transactional
    public Course findCourseWithId(Long id) {
        return courseRepository.findOne(id);
    }


    @Transactional
    public boolean registerStudentForCourse(Course course) {
        Student student = studentRepository.findOne(userAccountService.getCurrentLoggedInUser().getId());
        if (course.getStudentLimits() >= course.getStudents().size()) {
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
