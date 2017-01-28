package at.ac.tuwien.inso.service.student_subject_prefs;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Student;

public interface StudentSubjectPreferenceStore {

	/**
	 * adds the student and the course to the StudentSubjectPreferenceRepository
	 * means that the student has a preference for the subject of the course because he registered it
	 * 
	 * @param student should not be null and id should not be null
	 * @param course should not be null, should have an subject that is not null and that subject should have an id that is not null
	 */
    void studentRegisteredCourse(Student student, Course course);

    /**
     * removes the preference of a student for a subject (the subject for the given course) from the StudentSubjectPreferenceRepository
     * 
     * @param student should not be null and id should not be null
	 * @param course should not be null, should have an subject that is not null and that subject should have an id that is not null
     */
    void studentUnregisteredCourse(Student student, Course course);

    void studentGaveCourseFeedback(Student student, Feedback feedback);
}
