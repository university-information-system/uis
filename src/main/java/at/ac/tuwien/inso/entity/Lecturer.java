package at.ac.tuwien.inso.entity;

import javax.persistence.*;

@Entity
public class Lecturer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private UserProfile userProfile;

    protected Lecturer() {

    }

    public Lecturer(UserProfile userProfile) {
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

        Lecturer lecturer = (Lecturer) o;

        if (getId() != null ? !getId().equals(lecturer.getId()) : lecturer.getId() != null) return false;
        return getUserProfile() != null ? getUserProfile().equals(lecturer.getUserProfile()) : lecturer.getUserProfile() == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUserProfile() != null ? getUserProfile().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "id=" + id +
                ", userProfile=" + userProfile +
                '}';
    }
}
