package at.ac.tuwien.inso.service.student_subject_prefs;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Student;

public interface StudentSubjectPreferenceStore {

    void studentRegisteredCourse(Student student, Course course);

    void studentUnregisteredCourse(Student student, Course course);

    void studentGaveCourseFeedback(Student student, Feedback feedback);
}
