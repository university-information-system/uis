package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/registerToStudyplan")
public class AdminStudyPlanRegisterStudentController {

	@Autowired
	private StudyPlanService studyPlanService;
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping
    public String registerStudent(@RequestParam Long studentId, @RequestParam Long studyPlanId) {
		StudyPlan studyPlan = studyPlanService.findOne(studyPlanId);
		Student student = studentService.findOne(studentId);

		studentService.registerStudentToStudyPlan(student, studyPlan);
    	    	
    	return "redirect:/admin/users/"+student.getId();
    }
	
    @GetMapping(params = "studentToAddId")
    public String registerStudentView(@RequestParam Long studentToAddId, Model model) {
		Student student = studentService.findOne(studentToAddId);

		model.addAttribute("user", student);
    	model.addAttribute("test", "testString");
    	
    	List<StudyPlan> toShow = new ArrayList<StudyPlan>();
		for (StudyPlan sp : studyPlanService.findAll()) {
			boolean error = false;
    		for(StudyPlanRegistration studentSp : student.getStudyplans()){
    			if(sp.equals(studentSp.getStudyplan())){
	    			error = true;
	    		}
    		}
    		if(!error&&sp.isEnabled()){
    			toShow.add(sp);
    		}
    	}
    	
        model.addAttribute("studyPlans", toShow);
        
    	return "admin/addStudyPlanToStudent";
    }
}
