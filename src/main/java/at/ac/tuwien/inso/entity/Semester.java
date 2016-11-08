package at.ac.tuwien.inso.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Semester semester = (Semester) o;

        if (!id.equals(semester.id)) return false;
        return label.equals(semester.label);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + label.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Semester{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
