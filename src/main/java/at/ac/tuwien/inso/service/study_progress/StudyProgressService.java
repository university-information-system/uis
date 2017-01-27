package at.ac.tuwien.inso.service.study_progress;

import at.ac.tuwien.inso.entity.Student;

public interface StudyProgressService {

    /**
     * Get the current StudyProgress for a student
     *
     * @param student
     * @return
     */
    StudyProgress studyProgressFor(Student student);
}
