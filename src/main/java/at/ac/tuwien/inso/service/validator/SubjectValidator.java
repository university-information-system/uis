package at.ac.tuwien.inso.service.validator;

import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.exception.ValidationException;

public class SubjectValidator {

    public void validateSubjectId(Long id) {

        if(id == null || id < 1) {
            throw new ValidationException("Subject invalid id");
        }

    }

    public void validateNewSubject(Subject subject) {

        validateSubject(subject);

        if(subject.getName() == null || subject.getName().isEmpty()) {
            throw new ValidationException("Name of the subject cannot be empty");
        }

        if(subject.getEcts() == null) {
            throw new ValidationException("Ects of the subject cannot be empty");
        }

        if(subject.getEcts().doubleValue() <= 0) {
            throw new ValidationException("Ects of the subject needs to be greater than 0");
        }

    }

    private void validateSubject(Subject subject) {

        if(subject == null) {
            throw new ValidationException("Subject not found");
        }
    }
}
