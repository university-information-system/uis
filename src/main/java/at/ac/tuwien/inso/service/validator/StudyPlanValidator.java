package at.ac.tuwien.inso.service.validator;

import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.exception.ValidationException;

public class StudyPlanValidator {

    public void validateNewStudyPlan(StudyPlan studyPlan) {

        validateStudyPlan(studyPlan);

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

    private void validateStudyPlan(StudyPlan studyPlan) {

        if(studyPlan == null) {
            throw new ValidationException("No study plans found to create");
        }

    }

    public void validateStudyPlanId(Long id) {

        if(id == null || id < 1) {
            throw new ValidationException("Study plan invalid id");
        }

    }

    public void validateNewSubjectForStudyPlan(SubjectForStudyPlan subjectForStudyPlan) {

        if(subjectForStudyPlan == null) {
            throw new ValidationException("No subject found to add");
        }

        if(subjectForStudyPlan.getSubject() == null) {
            throw new ValidationException("Subject not found");
        }

        if(subjectForStudyPlan.getSubject().getId() == null || subjectForStudyPlan.getSubject().getId().intValue() < 1) {
            throw new ValidationException("Subject invalid id");
        }

        validateStudyPlan(subjectForStudyPlan.getStudyPlan());
        validateStudyPlanId(subjectForStudyPlan.getStudyPlan().getId());

    }

    public void validateRemovingSubjectFromStudyPlan(StudyPlan studyPlan, Subject subject) {

        if(subject == null) {
            throw new ValidationException("Subject not found");
        }

        if(subject.getId() == null || subject.getId().intValue() < 1) {
            throw new ValidationException("Subject invalid id");
        }

        validateStudyPlan(studyPlan);
        validateStudyPlanId(studyPlan.getId());
    }
}
