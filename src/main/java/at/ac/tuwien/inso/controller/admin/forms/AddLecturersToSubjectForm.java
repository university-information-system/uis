package at.ac.tuwien.inso.controller.admin.forms;


import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.service.LecturerService;
import at.ac.tuwien.inso.service.SubjectService;


public class AddLecturersToSubjectForm {

    public AddLecturersToSubjectForm() {
    }

    public AddLecturersToSubjectForm(Long id) {
        this.id = id;
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long toLecturerId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddLecturersToSubjectForm that = (AddLecturersToSubjectForm) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AddLecturersToSubjectForm{" +
                "id=" + id +
                '}';
    }
}
