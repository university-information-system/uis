package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SemesterService semesterService;

    @Override
    @Transactional
    public Student findOne(Long id) {
        return studentRepository.findOne(id);
    }

    @Override
    public Student findOne(UserAccount account) {
        return studentRepository.findByAccount(account);
    }

    @Override
    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudyPlanRegistration> findStudyPlanRegistrationsFor(Student student) {
        return student.getStudyplans();
    }

    @Override
    @Transactional
    public void registerStudentToStudyPlan(Student student, StudyPlan studyPlan) {
        registerStudentToStudyPlan(student, studyPlan, semesterService.getCurrentSemester());
    }

    @Override
    @Transactional
    public void registerStudentToStudyPlan(Student student, StudyPlan studyPlan, Semester currentSemester) {

        StudyPlanRegistration studyPlanRegistration = new StudyPlanRegistration(studyPlan, currentSemester);

        student.addStudyplans(studyPlanRegistration);

        studentRepository.save(student);
    }
}
