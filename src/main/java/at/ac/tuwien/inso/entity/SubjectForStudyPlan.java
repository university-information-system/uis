package at.ac.tuwien.inso.entity;

import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

@Embeddable
public class SubjectForStudyPlan {

    @ManyToMany
    private Subject subject;

    @ManyToMany
    private StudyPlan studyPlan;

}
