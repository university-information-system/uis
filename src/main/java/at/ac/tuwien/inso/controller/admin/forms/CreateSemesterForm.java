package at.ac.tuwien.inso.controller.admin.forms;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import at.ac.tuwien.inso.entity.Semester;

public class CreateSemesterForm {

    @NotEmpty
    private String label;

    public CreateSemesterForm() {
    }

    public CreateSemesterForm(String label) {
        this.label = label;
    }

    @NotNull
    public Semester toSemester() {
        return new Semester(getLabel());
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CreateSemesterForm that = (CreateSemesterForm) o;

        return label.equals(that.label);

    }

    @Override
    public String toString() {
        return "CreateSemesterForm{" +
                "label='" + label + '\'' +
                '}';
    }
}
