package at.ac.tuwien.inso.controller.forms.validation;

import javax.validation.*;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUsernameConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {

    String message() default "{UniqueUsername}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}