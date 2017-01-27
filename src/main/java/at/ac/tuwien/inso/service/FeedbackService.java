package at.ac.tuwien.inso.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Student;

@Service
public interface FeedbackService {

	/**
	 * returns all feedback of a student, student should not be null
	 * 
	 * @param student
	 * @return
	 */
    @PreAuthorize("isAuthenticated()")
    List<Feedback> findAllOfStudent(Student student);

    /**
     * saves a new feedback
     * @param feedback should not be null, feedback.suggestions and feedback.type should not be null. if there is no correct validation a ValidationException will be thrown
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    Feedback save(Feedback feedback);

    @PreAuthorize("isAuthenticated()")
    List<Feedback> findFeedbackForCourse(Long id);
}
