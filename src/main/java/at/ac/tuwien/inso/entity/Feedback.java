package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Entity
public class Feedback {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;

    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    protected Feedback() {}

    public Feedback(Student student, Course course) {
        this.student = student;
        this.course = course;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void addTags(Tag... tags) {
        this.tags.addAll(asList(tags));
    }

    public void removeTags(Tag... tags) {
        this.tags.removeAll(asList(tags));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feedback feedback = (Feedback) o;

        if (!id.equals(feedback.id)) return false;
        if (!student.equals(feedback.student)) return false;
        if (!course.equals(feedback.course)) return false;
        return tags.equals(feedback.tags);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + student.hashCode();
        result = 31 * result + course.hashCode();
        result = 31 * result + tags.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", student=" + student +
                ", course=" + course +
                ", tags=" + tags +
                '}';
    }
}
