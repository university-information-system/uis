package at.ac.tuwien.inso.service.student_subject_prefs;

import org.springframework.data.mongodb.repository.*;

public interface StudentSubjectPreferenceRepository extends MongoRepository<StudentSubjectPreference, String> {

    void deleteByStudentIdAndSubjectId(Long studentId, Long subjectId);

    StudentSubjectPreference findByStudentIdAndSubjectId(Long studentId, Long subjectId);
}
