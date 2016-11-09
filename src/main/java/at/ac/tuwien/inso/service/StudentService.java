package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    @Transactional(readOnly = true)
    public List<StudyPlanRegistration> findStudyPlanRegistrationsFor(Student student) {
        return student.getStudyplans();
    }
}
