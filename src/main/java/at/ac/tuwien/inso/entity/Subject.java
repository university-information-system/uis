package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.math.*;

@Entity
public class Subject {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal ects;

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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        if (id != null ? !id.equals(subject.id) : subject.id != null) return false;
        if (name != null ? !name.equals(subject.name) : subject.name != null) return false;
        return ects != null ? ects.equals(subject.ects) : subject.ects == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (ects != null ? ects.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ects=" + ects +
                '}';
    }
}
