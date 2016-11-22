package at.ac.tuwien.inso.controller.admin.forms.validation;

import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;

import javax.validation.*;

public class UniqueIdentificationNumberConstraintValidator implements ConstraintValidator<UniqueIdentificationNumber, String> {

    @Autowired
    private UisUserService uisUserService;

    public void initialize(UniqueIdentificationNumber constraint) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !uisUserService.existsUserWithIdentificationNumber(value);
    }
}
