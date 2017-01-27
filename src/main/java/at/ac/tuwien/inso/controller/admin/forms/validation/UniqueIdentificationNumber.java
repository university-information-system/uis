package at.ac.tuwien.inso.controller.admin.forms.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UniqueIdentificationNumberConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueIdentificationNumber {

    String message() default "{UniqueIdentificationNumber}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}