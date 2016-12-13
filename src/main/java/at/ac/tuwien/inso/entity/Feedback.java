package at.ac.tuwien.inso.entity;

import javax.persistence.*;

@Entity
public class Feedback {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Type type;
    @ManyToOne
    private Student student;
    @ManyToOne
    private Course course;

    protected Feedback() {}

    public Feedback(Student student, Course course, Type type) {
        this.student = student;
        this.course = course;
        this.type = type;
    }

    public Feedback(Student student, Course course) {
        this(student, course, Type.LIKE);
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Type getType() {
        return type;
    }

    public Feedback setType(Type type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feedback feedback = (Feedback) o;

        if (id != null ? !id.equals(feedback.id) : feedback.id != null) return false;
        if (type != feedback.type) return false;
        if (student != null ? !student.equals(feedback.student) : feedback.student != null) return false;
        return course != null ? course.equals(feedback.course) : feedback.course == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (student != null ? student.hashCode() : 0);
        result = 31 * result + (course != null ? course.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", type=" + type +
                ", student=" + student +
                ", course=" + course +
                '}';
    }

    public enum Type {
        LIKE,
        DISLIKE
    }
}
