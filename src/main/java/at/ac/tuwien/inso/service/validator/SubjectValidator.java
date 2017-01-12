package at.ac.tuwien.inso.service.validator;

import at.ac.tuwien.inso.exception.ValidationException;

public class SubjectValidator {

    public void validateSubjectId(Long id) {
        if(id == null || id < 1) {
            throw new ValidationException("Subject invalid id");
        }
    }
}
