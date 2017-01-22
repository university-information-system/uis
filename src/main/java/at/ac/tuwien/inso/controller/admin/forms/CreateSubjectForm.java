package at.ac.tuwien.inso.controller.admin.forms;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import at.ac.tuwien.inso.entity.Subject;

public class CreateSubjectForm {

    @NotEmpty
    private String name;

    @Min(1)
    @NotNull
    private BigDecimal ects;

    protected CreateSubjectForm() {}

    public CreateSubjectForm(String name, BigDecimal ects) {
        this.name = name;
        this.ects = ects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getEcts() {
        return ects;
    }

    public void setEcts(BigDecimal ects) {
        this.ects = ects;
    }

    public Subject toSubject() {
        return new Subject(name, ects);
    }
}
