package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;
	

    @Autowired
    private SemesterService semesterService;
	
	
    @Transactional(readOnly = true)
    public List<StudyPlanRegistration> findStudyPlanRegistrationsFor(Student student) {
        return student.getStudyplans();
    }
    
    @Transactional
    public Student findStudentById(Long id){
    	return studentRepository.findOne(id);	
    }
    
    public void registerStudentToStudyPlan(Student student, StudyPlan studyPlan){
    	registerStudentToStudyPlan(student, studyPlan, semesterService.getCurrentSemester());
    }
    
    public void registerStudentToStudyPlan(Student student, StudyPlan studyPlan, Semester currentSemester){

    	StudyPlanRegistration studyPlanRegistration = new StudyPlanRegistration(studyPlan, currentSemester);    
                        
    	student.addStudyplans(studyPlanRegistration);
    	
    	studentRepository.save(student);
    }
}
