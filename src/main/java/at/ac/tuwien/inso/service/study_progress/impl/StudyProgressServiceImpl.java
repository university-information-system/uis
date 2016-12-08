package at.ac.tuwien.inso.service.study_progress.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.study_progress.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

import static java.util.Collections.*;
import static java.util.Comparator.*;

@Service
public class StudyProgressServiceImpl implements StudyProgressService {

    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private FeedbackService feedbackService;

    @Override
    @Transactional(readOnly = true)
    public StudyProgress studyProgressFor(Student student) {
        Semester currentSemester = semesterService.getCurrentSemester();

        List<Semester> semesters = studentSemesters(student);
        List<Course> courses = courseService.findAllForStudent(student);
        List<Grade> grades = gradeService.findAllOfStudent(student);
        List<Feedback> feedbacks = feedbackService.findAllOfStudent(student);

        List<SemesterProgress> semestersProgress = semesters.stream()
                .map(it -> new SemesterProgress(it, courseRegistrations(it, currentSemester, courses, grades, feedbacks)))
                .collect(Collectors.toList());

        return new StudyProgress(currentSemester, semestersProgress);
    }

    private List<Semester> studentSemesters(Student student) {
        return getFirstSemesterFor(student)
                .map(it -> semesterService.findAllSince(it))
                .orElse(emptyList());
    }

    private Optional<Semester> getFirstSemesterFor(Student student) {
        return student.getStudyplans().stream()
                .map(StudyPlanRegistration::getRegisteredSince)
                .min(comparing(Semester::getId));
    }

    private List<CourseRegistration> courseRegistrations(Semester semester, Semester currentSemester, List<Course> courses, List<Grade> grades, List<Feedback> feedbacks) {
        return courses.stream()
                .filter(it -> it.getSemester().equals(semester))
                .map(it -> new CourseRegistration(it, courseRegistrationState(it, currentSemester, grades, feedbacks)))
                .collect(Collectors.toList());
    }

    private CourseRegistrationState courseRegistrationState(Course course, Semester currentSemester, List<Grade> grades, List<Feedback> feedbacks) {
        Optional<Grade> grade = grades.stream().filter(it -> it.getCourse().equals(course)).findFirst();
        Optional<Feedback> feedback = feedbacks.stream().filter(it -> it.getCourse().equals(course)).findFirst();

        if (feedback.isPresent() && grade.isPresent()) {
            return grade.get().getMark().isPositive() ? CourseRegistrationState.complete_ok : CourseRegistrationState.complete_not_ok;
        } else if (feedback.isPresent()) {
            return CourseRegistrationState.needs_grade;
        } else if (grade.isPresent()) {
            return CourseRegistrationState.needs_feedback;
        } else {
            return course.getSemester().equals(currentSemester) ? CourseRegistrationState.in_progress : CourseRegistrationState.needs_feedback;
        }
    }
}
