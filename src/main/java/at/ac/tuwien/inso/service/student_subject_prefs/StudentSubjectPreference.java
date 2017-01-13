package at.ac.tuwien.inso.service.student_subject_prefs;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

@Document
public class StudentSubjectPreference {

    @Id
    public String id;

    public Long studentId;

    public Long subjectId;

    public Double preferenceValue;

    protected StudentSubjectPreference() {

    }

    public StudentSubjectPreference(Long studentId, Long subjectId, Double preferenceValue) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.preferenceValue = preferenceValue;
    }

    @Override
    public String toString() {
        return "StudentSubjectPreference{" +
                "id='" + id + '\'' +
                ", studentId=" + studentId +
                ", subjectId=" + subjectId +
                ", preferenceValue=" + preferenceValue +
                '}';
    }
}
