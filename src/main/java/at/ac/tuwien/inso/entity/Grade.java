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

    protected Grade() {

    }

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

        if (id != null) {
            if (!id.equals(grade.id)) return false;
        } else {
            if (grade.id != null) {
                return false;
            }
        }
        if (!course.equals(grade.course)) return false;
        if (!lecturer.equals(grade.lecturer)) return false;
        if (!student.equals(grade.student)) return false;
        return mark.equals(grade.mark);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = course != null ? 31 * result + course.hashCode() : 0;
        result = lecturer != null ? 31 * result + lecturer.hashCode() : 0;
        result = student != null ? 31 * result + student.hashCode() : 0;
        result = mark != null ? 31 * result + mark.hashCode() : 0;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setMark(BigDecimal mark) {
        this.mark = mark;
    }
}
