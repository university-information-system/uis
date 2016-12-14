package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private CourseService courseService;

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> findAllOfStudent(Student student) {
        return feedbackRepository.findAllOfStudent(student);
    }

    @Override
    @Transactional
    public Feedback save(Feedback feedback) {
        log.info("Creating feedback from student {} for course {}: {} - {}",
                feedback.getStudent().getId(),
                feedback.getCourse().getId(),
                feedback.getType(),
                feedback.getSuggestions());

        guardSingleFeedback(feedback);
        guardStudentRegisteredForCourse(feedback.getStudent(), feedback.getCourse());

        return feedbackRepository.save(feedback);
    }

    private void guardSingleFeedback(Feedback feedback) {
        List<Course> feedbackCourses = findAllOfStudent(feedback.getStudent())
                .stream()
                .map(Feedback::getCourse)
                .collect(Collectors.toList());
        if (feedbackCourses.contains(feedback.getCourse())) {
            throw new ActionNotAllowedException("Giving feedback multiple times for the same course is not allowed");
        }
    }

    private void guardStudentRegisteredForCourse(Student student, Course course) {
        List<Course> studentCourses = courseService.findAllForStudent(student);

        if (!studentCourses.contains(course)) {
            throw new ActionNotAllowedException("Student tried to give feedback for course he is not registered for");
        }
    }
}
