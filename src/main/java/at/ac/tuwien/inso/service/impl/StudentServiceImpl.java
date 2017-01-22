package at.ac.tuwien.inso.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.service.SemesterService;
import at.ac.tuwien.inso.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {


	private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
	
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SemesterService semesterService;

    @Override
    @Transactional
    public Student findOne(Long id) {
    	log.info("finding student by id "+id);
        return studentRepository.findOne(id);
    }

    @Override
    public Student findOne(UserAccount account) {
    	log.info("finding student by account "+account.toString());
        return studentRepository.findByAccount(account);
    }

    @Override
    public Student findByUsername(String username) {
    	log.info("finding student by username "+username);
        return studentRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudyPlanRegistration> findStudyPlanRegistrationsFor(Student student) {
    	log.info("finding studyplanregistrations for student  "+student.toString());
        return student.getStudyplans();
    }

    @Override
    @Transactional
    public void registerStudentToStudyPlan(Student student, StudyPlan studyPlan) {
        log.info("for current semester, registering student "+student.toString()+" to StudyPlan "+studyPlan.toString());

        SemesterDto semester = semesterService.getOrCreateCurrentSemester();

        registerStudentToStudyPlan(student, studyPlan, semester);
    }

    @Override
    @Transactional
    public void registerStudentToStudyPlan(Student student, StudyPlan studyPlan, SemesterDto currentSemesterDto) {
    	log.info("for semester "+currentSemesterDto+" registering student "+student.toString()+" to StudyPlan "+studyPlan.toString());
        
    	Semester currentSemester = currentSemesterDto.toEntity();
    	
        StudyPlanRegistration studyPlanRegistration = new StudyPlanRegistration(studyPlan, currentSemester);

        student.addStudyplans(studyPlanRegistration);

        studentRepository.save(student);
    }
}
