package at.ac.tuwien.inso.controller.admin.forms;


import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import at.ac.tuwien.inso.entity.EctsDistribution;
import at.ac.tuwien.inso.entity.StudyPlan;

public class CreateStudyPlanForm {

    @NotEmpty
    private String name;

    @Min(1)
    @NotNull
    private BigDecimal mandatory;

    @Min(1)
    @NotNull
    private BigDecimal optional;

    @Min(1)
    @NotNull
    private BigDecimal freeChoice;

    protected CreateStudyPlanForm() {}

    public CreateStudyPlanForm(String name, BigDecimal mandatory, BigDecimal optional, BigDecimal freeChoice) {
        this.name = name;
        this.mandatory = mandatory;
        this.optional = optional;
        this.freeChoice = freeChoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMandatory() {
        return mandatory;
    }

    public void setMandatory(BigDecimal mandatory) {
        this.mandatory = mandatory;
    }

    public BigDecimal getOptional() {
        return optional;
    }

    public void setOptional(BigDecimal optional) {
        this.optional = optional;
    }

    public BigDecimal getFreeChoice() {
        return freeChoice;
    }

    public void setFreeChoice(BigDecimal freeChoice) {
        this.freeChoice = freeChoice;
    }

    public StudyPlan toStudyPlan() {
        return new StudyPlan(name, new EctsDistribution(mandatory, optional, freeChoice));
    }
}
