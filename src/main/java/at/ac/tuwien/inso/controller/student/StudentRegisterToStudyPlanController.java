package at.ac.tuwien.inso.controller.student;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.StudyPlanService;

@Controller
@RequestMapping("/admin/registerToStudyplan")
public class StudentRegisterToStudyPlanController {

	@Autowired
	private StudyPlanService studyPlanService;
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping
    public String registerStudent(@RequestParam Long studentId, @RequestParam Long studyPlanId) {
    	StudyPlan studyPlan = studyPlanService.getStudyPlanById(studyPlanId);
    	Student student = studentService.findStudentById(studentId);
    	
    	studentService.registerStudentToStudyPlan(student, studyPlan);
    	    	
    	return "redirect:/admin/users/"+student.getId();
    }
	
    @GetMapping(params = "studentToAddId")
    public String registerStudentView(@RequestParam Long studentToAddId, Model model) {
    	Student student = studentService.findStudentById(studentToAddId);
    	
    	model.addAttribute("user", student);
    	model.addAttribute("test", "testString");
    	
    	List<StudyPlan> toShow = new ArrayList<StudyPlan>();
    	for(StudyPlan sp:studyPlanService.getAllStudyPlans()){
    		boolean error = false;
    		for(StudyPlanRegistration studentSp : student.getStudyplans()){
    			if(sp.equals(studentSp.getStudyplan())){
	    			error = true;
	    		}
    		}
    		if(!error){
    			toShow.add(sp);
    		}
    	}
    	
        model.addAttribute("studyPlans", toShow);
        
    	return "admin/addStudyPlanToStudent";
    }
}
