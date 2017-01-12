package at.ac.tuwien.inso.service.validator;

import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.exception.ValidationException;

public class FeedbackValidator {


    public void validateNewFeedback(Feedback feedback) {

        if(feedback == null) {
            throw new ValidationException("Feedback not found");
        }

        if(feedback.getSuggestions() == null) {
            throw new ValidationException("Suggestions for feedback not found");
        }

        if(feedback.getType() == null) {
            throw new ValidationException("Type for feedback not found");
        }

    }

    public void validateCourseId(Long id) {

        if(id == null || id < 1) {
            throw new ValidationException("Course id invalid");
        }

    }

}
