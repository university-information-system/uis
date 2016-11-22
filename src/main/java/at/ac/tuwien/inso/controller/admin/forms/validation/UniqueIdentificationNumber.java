package at.ac.tuwien.inso.controller.admin.forms.validation;

import javax.validation.*;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueIdentificationNumberConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueIdentificationNumber {

    String message() default "{UniqueIdentificationNumber}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}