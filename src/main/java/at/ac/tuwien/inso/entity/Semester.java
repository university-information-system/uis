package at.ac.tuwien.inso.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import at.ac.tuwien.inso.dto.SemesterDto;

@Entity
public class Semester {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String label;

    protected Semester() {

    }

    public Semester(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id){
    	this.id = id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Semester semester = (Semester) o;

        if (id != null ? !id.equals(semester.id) : semester.id != null) return false;
        return label != null ? label.equals(semester.label) : semester.label == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Semester{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
    
    public SemesterDto toDto(){
    	SemesterDto dto = new SemesterDto(label);
    	if(id!=null){dto.setId(id);}
		return dto;
    }
}
