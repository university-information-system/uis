package at.ac.tuwien.inso.service.validator;

public class ValidatorFactory {

    public StudyPlanValidator getStudyPlanValidator() {
        return new StudyPlanValidator();
    }

    public SubjectValidator getSubjectValidator() {
        return new SubjectValidator();
    }

    public UisUserValidator getUisUserValidator() {
        return new UisUserValidator();
    }
}
