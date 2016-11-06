package at.ac.tuwien.inso.entity;

import javax.persistence.*;

@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private UserProfile userProfile;

    protected Student() {

    }

    public Student(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Long getId() {
        return id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != null ? !id.equals(student.id) : student.id != null) return false;
        return userProfile != null ? userProfile.equals(student.userProfile) : student.userProfile == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userProfile != null ? userProfile.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", userProfile=" + userProfile +
                '}';
    }
}
