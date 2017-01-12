package at.ac.tuwien.inso.service.validator;

public class ValidatorFactory {

    public StudyPlanValidator getStudyPlanValidator() {
        return new StudyPlanValidator();
    }
}
