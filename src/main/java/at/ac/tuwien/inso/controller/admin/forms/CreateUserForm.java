package at.ac.tuwien.inso.controller.admin.forms;

import at.ac.tuwien.inso.entity.*;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;

public class CreateUserForm {

    public static final String STUDENT = "Student";
    public static final String LECTURER = "Lecturer";

    @Pattern(regexp = STUDENT + "|" + LECTURER)
    private String type;

    @NotEmpty
    private String name;

    @Email
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
