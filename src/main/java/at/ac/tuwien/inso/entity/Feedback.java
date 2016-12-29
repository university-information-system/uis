package at.ac.tuwien.inso.entity;

import javax.persistence.*;

@Entity
public class Feedback {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Type type;

    @Column(length = 1024)
    private String suggestions;

    @ManyToOne(optional = false)
    private Student student;

    @ManyToOne(optional = false)
    private Course course;

    protected Feedback() {}

    public Feedback(Student student, Course course, Type type) {
        this(student, course, type, "");
    }

    public Feedback(Student student, Course course, Type type, String suggestions) {
        this.student = student;
        this.course = course;
        this.type = type;
        this.suggestions = suggestions;
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

    public String getSuggestions() {
        return suggestions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feedback feedback = (Feedback) o;

        if (getId() != null ? !getId().equals(feedback.getId()) : feedback.getId() != null) return false;
        if (getType() != feedback.getType()) return false;
        if (getSuggestions() != null ? !getSuggestions().equals(feedback.getSuggestions()) : feedback.getSuggestions() != null)
            return false;
        if (getStudent() != null ? !getStudent().equals(feedback.getStudent()) : feedback.getStudent() != null)
            return false;
        return getCourse() != null ? getCourse().equals(feedback.getCourse()) : feedback.getCourse() == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getSuggestions() != null ? getSuggestions().hashCode() : 0);
        result = 31 * result + (getStudent() != null ? getStudent().hashCode() : 0);
        result = 31 * result + (getCourse() != null ? getCourse().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", type=" + type +
                ", suggestions='" + suggestions + '\'' +
                ", student=" + student +
                ", course=" + course +
                '}';
    }

    public enum Type {
        LIKE,
        DISLIKE
    }
}
