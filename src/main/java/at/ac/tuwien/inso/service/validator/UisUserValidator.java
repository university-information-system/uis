package at.ac.tuwien.inso.service.validator;

import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.exception.ValidationException;

public class UisUserValidator {

    public void validateNewUisUser(UisUser uisUser) {

        if (uisUser == null) {
            throw new ValidationException("User not found");
        }

        if (uisUser.getName() == null || uisUser.getName().isEmpty()) {
            throw new ValidationException("Name of user cannot be empty");
        }

        if (uisUser.getIdentificationNumber() == null || uisUser.getIdentificationNumber().isEmpty()) {
            throw new ValidationException("Identification number of user cannot be empty");
        }

        if (uisUser.getEmail() == null || uisUser.getEmail().isEmpty()) {
            throw new ValidationException("Email of user cannot be empty");
        }

        //TODO please validate the email
    }

    public void validateUisUserId(Long id) {
        if(id == null || id < 1) {
            throw new ValidationException("User invalid id");
        }
    }
}
