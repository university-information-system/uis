package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.service.SemesterService;
import at.ac.tuwien.inso.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
