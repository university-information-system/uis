package at.ac.tuwien.inso.controller.forms.validation;

import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;

import javax.validation.*;

public class UniqueUsernameConstraintValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserAccountService userAccountService;

    public void initialize(UniqueUsername constraint) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !userAccountService.existsUsername(value);
    }
}
