package at.ac.tuwien.inso.controller.admin.forms;

import at.ac.tuwien.inso.entity.*;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;

public class CreateUserForm {

    public static final String STUDENT = "Student";
    public static final String LECTURER = "Lecturer";

    @Pattern(regexp = STUDENT + "|" + LECTURER)
    private String type = STUDENT;

    @NotEmpty
    private String name;

    // RFC 2822 email pattern
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String email;

    protected CreateUserForm() {
    }

    public CreateUserForm(String type, String name, String email) {
        this.type = type;
        this.name = name;
        this.email = email;
    }

    public UisUser toUisUser() {
        if (type.equals(CreateUserForm.STUDENT)) {
            return new Student(name, email);
        } else {
            return new Lecturer(name, email);
        }
    }

    public String getType() {
        return type;
    }

    public CreateUserForm setType(String type) {
        this.type = type;
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

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return email != null ? email.equals(that.email) : that.email == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CreateUserForm{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
