package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.math.*;
import java.util.ArrayList;
import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;

@Entity
public class Subject {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal ects;

    @ManyToMany
    private List<Lecturer> lecturers = new ArrayList<>();

    protected Subject() {
    }

    public Subject(String name, BigDecimal ects) {
        this.name = name;
        this.ects = ects;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getEcts() {
        return ects;
    }

    public List<Lecturer> getLecturers() {
        return unmodifiableList(lecturers);
    }

    public void addLecturers(Lecturer... lecturers) {
        this.lecturers.addAll(asList(lecturers));
    }

    public void removeLecturers(Lecturer... lecturers) {
        this.lecturers.removeAll(asList(lecturers));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        if (getId() != null ? !getId().equals(subject.getId()) : subject.getId() != null) return false;
        if (getName() != null ? !getName().equals(subject.getName()) : subject.getName() != null) return false;
        if (getEcts() != null ? !getEcts().equals(subject.getEcts()) : subject.getEcts() != null) return false;
        return getLecturers() != null ? getLecturers().equals(subject.getLecturers()) : subject.getLecturers() == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getEcts() != null ? getEcts().hashCode() : 0);
        result = 31 * result + (getLecturers() != null ? getLecturers().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ects=" + ects +
                ", lecturers=" + lecturers +
                '}';
    }
}
