package at.ac.tuwien.inso.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.StudyPlanService;

@Controller
@RequestMapping("/admin/registerToStudyplan")
public class StudentRegisterToStudyPlan {

	@Autowired
	private StudyPlanService studyPlanService;
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping
    public String registerStudent(@RequestParam Long studentId, @RequestParam Long studyPlanId) {
    	System.out.println("hier geht er rein");
    	System.out.println("id"+studentId);
    	StudyPlan studyPlan = studyPlanService.getStudyPlanById(studyPlanId);
    	Student student = studentService.findStudentById(studentId);
    	
    	studentService.registerStudentToStudyPlan(student, studyPlan);
    	    	
    	return "admin/users/"+studentId;
    }
	
    @GetMapping(params = "studentId")
    public String registerStudentView(@RequestParam Long studentId, Model model) {
    	UisUser student = studentService.findStudentById(studentId);
    	
    	model.addAttribute("user", student);
    	
    	model.addAttribute("test", "testString");
    	
        //model.addAttribute("studyPlans", studyPlanService.getAllStudyPlans());
        
    	return "admin/addStudyPlanToStudent";
    }
    
    @ModelAttribute("studyPlans")
    private Iterable<StudyPlan> getStudyPlans() {
    	System.out.println("get all studyplans");
        return studyPlanService.getAllStudyPlans();
    }
    
    /*@GetMapping
    private String registerStudent2() {
    	System.out.println("hier geht er rein2");
    	return "null";
    }*/
}
