package at.ac.tuwien.inso.service.validator;

import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;

public class StudyPlanValidator {

    public void validateNewStudyPlan(StudyPlan studyPlan) {

        if(studyPlan == null) {
            throw new BusinessObjectNotFoundException("No study plans found to create");
        }

        if(studyPlan.getName() == null || studyPlan.getName().isEmpty()) {
            throw new ValidationException("Name of the study plan cannot be empty");
        }

        if(studyPlan.getEctsDistribution().getMandatory() == null) {
            throw new ValidationException("Mandatory ects of the study plan cannot be empty");
        }

        if(studyPlan.getEctsDistribution().getMandatory().intValue() <= 0) {
            throw new ValidationException("Mandatory ects of the study plan needs to be greater than 0");
        }

        if(studyPlan.getEctsDistribution().getOptional() == null) {
            throw new ValidationException("Optional ects of the study plan cannot be empty");
        }

        if(studyPlan.getEctsDistribution().getOptional().intValue() <= 0) {
            throw new ValidationException("Optional ects of the study plan needs to be greater than 0");
        }

        if(studyPlan.getEctsDistribution().getFreeChoice() == null) {
            throw new ValidationException("Free choice ects of the study plan cannot be empty");
        }

        if(studyPlan.getEctsDistribution().getFreeChoice().intValue() <= 0) {
            throw new ValidationException("Free choice ects of the study plan needs to be greater than 0");
        }
    }

    public void validateStudyPlanId(Long id) {
        if(id == null || id < 1) {
            throw new ValidationException("Study plan invalid id");
        }
    }
}
