package at.ac.tuwien.inso.controller.admin.forms;

import at.ac.tuwien.inso.controller.admin.forms.validation.*;
import at.ac.tuwien.inso.entity.*;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;

public class CreateUserForm {

    public static final String STUDENT = "Student";
    public static final String LECTURER = "Lecturer";

    @Pattern(regexp = STUDENT + "|" + LECTURER)
    private String type = STUDENT;

    @NotEmpty
    @UniqueIdentificationNumber
    private String identificationNumber;

    @NotEmpty
    private String name;

    // RFC 2822 email pattern
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String email;

    protected CreateUserForm() {
    }

    public CreateUserForm(String type, String identificationNumber, String name, String email) {
        this.type = type;
        this.identificationNumber = identificationNumber;
        this.name = name;
        this.email = email;
    }

    public UisUser toUisUser() {
        if (type.equals(CreateUserForm.STUDENT)) {
            return new Student(identificationNumber, name, email);
        } else {
            return new Lecturer(identificationNumber, name, email);
        }
    }

    public String getType() {
        return type;
    }

    public CreateUserForm setType(String type) {
        this.type = type;
        return this;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public CreateUserForm setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateUserForm setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateUserForm setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateUserForm that = (CreateUserForm) o;

        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getIdentificationNumber() != null ? !getIdentificationNumber().equals(that.getIdentificationNumber()) : that.getIdentificationNumber() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getEmail() != null ? getEmail().equals(that.getEmail()) : that.getEmail() == null;

    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getIdentificationNumber() != null ? getIdentificationNumber().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CreateUserForm{" +
                "type='" + type + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
