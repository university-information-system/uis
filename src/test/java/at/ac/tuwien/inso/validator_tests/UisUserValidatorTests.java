package at.ac.tuwien.inso.validator_tests;


import org.junit.Test;

import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.validator.UisUserValidator;


public class UisUserValidatorTests {

    private final UisUserValidator validator = new UisUserValidator();

    @Test
    public void testEmailValidator() {
        validator.validateEmail("uis@tuwien.ac.at");
    }

    @Test (expected = ValidationException.class)
    public void testEmailInvalid() {
        validator.validateEmail("uis");
    }

    /**
     * Local email (for intranets) should not be allowed
     */
    @Test (expected = ValidationException.class)
    public void testEmailLocal() {
        validator.validateEmail("uis@tuwien");
    }

    @Test (expected = ValidationException.class)
    public void testEmailNoLocalPart() {
        validator.validateEmail("@tuwien.at");
    }

    @Test (expected = ValidationException.class)
    public void testEmailValidatorNull() {
        validator.validateEmail(null);
    }

    @Test (expected = ValidationException.class)
    public void testEmailValidatorEmpty() {
        validator.validateEmail("");
    }

    /**
     * German umlauts are valid in emails
     */
    @Test
    public void testEmailValidatorUmlauts() {
        validator.validateEmail("björn@tüdlü.at");
    }
}
