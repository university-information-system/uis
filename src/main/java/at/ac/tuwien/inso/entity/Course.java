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

    @Column(nullable = false)
    private int studentLimits;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany
    private List<Student> students = new ArrayList<>();

    protected Course() {
    }

    public Course(Subject subject, Semester semester) {
        this(subject, semester, "");
    }

    public Course(Subject subject, Semester semester, String description) {
        this.subject = subject;
        this.semester = semester;
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public void setDescription(String description) {
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

    public int getStudentLimits() {
        return studentLimits;
    }

    public void setStudentLimits(int studentLimits) {
        this.studentLimits = studentLimits;
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
        if (studentLimits != course.studentLimits) return false;
        if (!tags.equals(course.tags)) return false;
        return students.equals(course.students);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + subject.hashCode();
        result = 31 * result + semester.hashCode();
        result = 31 * result + studentLimits;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (students != null ? students.hashCode() : 0);
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
                ", student limits=" + studentLimits +
                ", students=" + students +
                '}';
    }
}