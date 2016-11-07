package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Grade {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Lecturer lecturer;

    @ManyToOne
    private Student student;

    @Column
    private BigDecimal mark;

    public Grade(Course course, Lecturer lecturer, Student student, BigDecimal mark) {
        this.course = course;
        this.lecturer = lecturer;
        this.student = student;
        this.mark = mark;
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public Student getStudent() {
        return student;
    }

    public BigDecimal getMark() {
        return mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade = (Grade) o;

        if (!id.equals(grade.id)) return false;
        if (!course.equals(grade.course)) return false;
        if (!lecturer.equals(grade.lecturer)) return false;
        if (!student.equals(grade.student)) return false;
        return mark.equals(grade.mark);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + course.hashCode();
        result = 31 * result + lecturer.hashCode();
        result = 31 * result + student.hashCode();
        result = 31 * result + mark.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", course=" + course +
                ", lecturer=" + lecturer +
                ", student=" + student +
                ", mark=" + mark +
                '}';
    }
}
