package at.ac.tuwien.inso.controller.admin.forms;


public class AddLecturersToSubjectForm {

    public AddLecturersToSubjectForm() {
    }

    public AddLecturersToSubjectForm(Long lecturerId) {
        this.lecturerId = lecturerId;
    }

    private Long lecturerId;

    public Long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Long lecturerId) {
        this.lecturerId = lecturerId;
    }

    public Long toLecturerId() {
        return lecturerId;
    }

    @Override
    public String toString() {
        return "AddLecturersToSubjectForm{" +
                "lecturerId=" + lecturerId +
                '}';
    }
}
