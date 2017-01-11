package at.ac.tuwien.inso.service.student_subject_prefs;

import at.ac.tuwien.inso.entity.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class StudentSubjectPreferenceStoreImpl implements StudentSubjectPreferenceStore {

    private static final Logger log = LoggerFactory.getLogger(StudentSubjectPreferenceStoreImpl.class);

    @Autowired
    private StudentSubjectPreferenceRepository preferenceRepository;

    @Override
    public void studentRegisteredCourse(Student student, Course course) {
        StudentSubjectPreference preference = new StudentSubjectPreference(student.getId(), course.getSubject().getId(), 1.0);

        log.debug("Storing student subject preference due to course registration: " + preference);

        preferenceRepository.insert(preference);
    }

    @Override
    public void studentUnregisteredCourse(Student student, Course course) {
        log.debug("Remove student subject preference due to course unregistration: " + student + ", " + course);

        preferenceRepository.deleteByStudentIdAndSubjectId(student.getId(), course.getSubject().getId());
    }

    @Override
    public void studentGaveCourseFeedback(Student student, Feedback feedback) {
        StudentSubjectPreference preference =
                preferenceRepository.findByStudentIdAndSubjectId(student.getId(), feedback.getCourse().getSubject().getId());

        preference.preferenceValue = feedback.getType() == Feedback.Type.LIKE ? 2.0 : -1.0;

        log.debug("Updating student subject preference due to course feedback: " + preference);

        preferenceRepository.save(preference);
    }
}
