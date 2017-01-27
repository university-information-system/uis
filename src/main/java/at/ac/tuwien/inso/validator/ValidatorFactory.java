package at.ac.tuwien.inso.validator;

public class ValidatorFactory {

    public StudyPlanValidator getStudyPlanValidator() {
        return new StudyPlanValidator();
    }

    public SubjectValidator getSubjectValidator() {
        return new SubjectValidator();
    }

    public CourseValidator getCourseValidator() {
        return new CourseValidator();
    }

    public UisUserValidator getUisUserValidator() {
        return new UisUserValidator();
    }

    public FeedbackValidator getFeedbackValidator() {
        return new FeedbackValidator();
    }
}
