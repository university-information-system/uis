package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;

@Entity
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Subject subject;

    @ManyToOne(optional = false)
    private Semester semester;

    @Column(nullable = false)
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany
    private List<Student> students = new ArrayList<>();

    protected Course() {}

    public Course(Subject subject, Semester semester) {
        this(subject, semester, "");
    }

    public Course(Subject subject, Semester semester, String description) {
        this.subject = subject;
        this.semester = semester;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public Semester getSemester() {
        return semester;
    }

    public String getDescription() {
        return description;
    }

    public List<Tag> getTags() {
        return unmodifiableList(tags);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addTags(Tag... tags) {
        this.tags.addAll(asList(tags));
    }

    public void removeTags(Tag... tags) {
        this.tags.removeAll(asList(tags));
    }

    public void addStudents(Student... students) {
        this.students.addAll(asList(students));
    }

    public void removeStudents(Student... students) {
        this.students.removeAll(asList(students));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (!id.equals(course.id)) return false;
        if (!subject.equals(course.subject)) return false;
        if (!semester.equals(course.semester)) return false;
        if (!description.equals(course.description)) return false;
        if (!tags.equals(course.tags)) return false;
        return students.equals(course.students);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + subject.hashCode();
        result = 31 * result + semester.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + tags.hashCode();
        result = 31 * result + students.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", subject=" + subject +
                ", semester=" + semester +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", students=" + students +
                '}';
    }
}