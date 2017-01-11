package at.ac.tuwien.inso.service.student_subject_prefs;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

@Document(collection = "user_subject_prefs")
public class StudentSubjectPreference {

    @Id
    public String id;

    @Field("user_id")
    public Long studentId;

    @Field("item_id")
    public Long subjectId;

    @Field("preference")
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
