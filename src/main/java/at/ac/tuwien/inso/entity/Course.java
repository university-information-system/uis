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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tag> getTags() {
        return unmodifiableList(tags);
    }

    public List<Student> getStudents() {
        return students;
    }

    public Course addTags(Tag... tags) {
        this.tags.addAll(asList(tags));
        return this;
    }

    public void removeTags(List<Tag> tags) {
        this.tags.removeAll(tags);
    }

    public Course addStudents(Student... students) {
        this.students.addAll(asList(students));
        return this;
    }

    public void removeStudents(Student... students) {
        this.students.removeAll(asList(students));
    }

    public int getStudentLimits() {
        return studentLimits;
    }

    public Course setStudentLimits(int studentLimits) {
        this.studentLimits = studentLimits;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (studentLimits != course.studentLimits) return false;
        if (id != null ? !id.equals(course.id) : course.id != null) return false;
        if (subject != null ? !subject.equals(course.subject) : course.subject != null) return false;
        if (semester != null ? !semester.equals(course.semester) : course.semester != null) return false;
        if (description != null ? !description.equals(course.description) : course.description != null) return false;
        if (tags != null ? !tags.equals(course.tags) : course.tags != null) return false;
        return students != null ? students.equals(course.students) : course.students == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (semester != null ? semester.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + studentLimits;
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