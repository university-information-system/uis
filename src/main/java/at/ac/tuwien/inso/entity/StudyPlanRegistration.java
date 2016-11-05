package at.ac.tuwien.inso.entity;

import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

@Embeddable
public class StudyPlanRegistration {

    @ManyToMany
    private StudyPlan studyPlan;

    @ManyToMany
    private Student student;
}
