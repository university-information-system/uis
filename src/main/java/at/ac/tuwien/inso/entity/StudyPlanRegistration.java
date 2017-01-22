package at.ac.tuwien.inso.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import at.ac.tuwien.inso.dto.StudyPlanRegistrationDto;

@Entity
public class StudyPlanRegistration {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private StudyPlan studyplan;

    @ManyToOne(optional = false)
    private Semester registeredSince;

    protected StudyPlanRegistration() {

    }

    public StudyPlanRegistration(StudyPlan studyplan, Semester registeredSince) {
        this.studyplan = studyplan;
        this.registeredSince = registeredSince;
    }

    public Long getId() {
        return id;
    }

    public StudyPlan getStudyplan() {
        return studyplan;
    }

    public Semester getRegisteredSince() {
        return registeredSince;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyPlanRegistration that = (StudyPlanRegistration) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (studyplan != null ? !studyplan.equals(that.studyplan) : that.studyplan != null) return false;
        return registeredSince != null ? registeredSince.equals(that.registeredSince) : that.registeredSince == null;

    }
    
    public StudyPlanRegistrationDto toDto(){
    	StudyPlanRegistrationDto dto = new StudyPlanRegistrationDto();
    	dto.setRegisteredSince(registeredSince.toDto());
    	dto.setStudyplan(studyplan);
    	return dto;
    	
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (studyplan != null ? studyplan.hashCode() : 0);
        result = 31 * result + (registeredSince != null ? registeredSince.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StudyPlanRegistration{" +
                "id=" + id +
                ", studyplan=" + studyplan +
                ", registeredSince=" + registeredSince +
                '}';
    }
}
