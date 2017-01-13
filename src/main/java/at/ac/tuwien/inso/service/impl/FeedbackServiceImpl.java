package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.validator.FeedbackValidator;
import at.ac.tuwien.inso.service.validator.ValidatorFactory;
import at.ac.tuwien.inso.service.student_subject_prefs.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);
    private ValidatorFactory validatorFactory = new ValidatorFactory();
    private FeedbackValidator validator = validatorFactory.getFeedbackValidator();

    @Autowired
    StudentSubjectPreferenceStore studentSubjectPreferenceStore;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> findAllOfStudent(Student student) {
        return feedbackRepository.findAllOfStudent(student);
    }

    @Override
    @Transactional
    public Feedback save(Feedback feedback) {
        validator.validateNewFeedback(feedback);
        log.info("Creating feedback from student {} for course {}: {} - {}",
                feedback.getStudent().getId(),
                feedback.getCourse().getId(),
                feedback.getType(),
                feedback.getSuggestions());

        guardSingleFeedback(feedback);
        guardStudentRegisteredForCourse(feedback.getStudent(), feedback.getCourse());

        studentSubjectPreferenceStore.studentGaveCourseFeedback(feedback.getStudent(), feedback);

        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> findFeedbackForCourse(Long id) {
        validator.validateCourseId(id);
        return feedbackRepository.findByCourseId(id);
    }

    private void guardSingleFeedback(Feedback feedback) {
        if (feedbackRepository.exists(feedback)) {
            throw new ActionNotAllowedException("Giving feedback multiple times for the same course is not allowed");
        }
    }

    private void guardStudentRegisteredForCourse(Student student, Course course) {
        if (!courseRepository.existsCourseRegistration(student, course)) {
            throw new ActionNotAllowedException("Student tried to give feedback for course he is not registered for");
        }
    }
}
