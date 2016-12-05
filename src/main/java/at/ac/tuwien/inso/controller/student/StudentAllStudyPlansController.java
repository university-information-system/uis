package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.service.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/all-studyplans")
public class StudentAllStudyPlansController {

    @Autowired
    private StudyPlanService studyPlanService;

    @GetMapping
    public String listStudyPlans() {
        return "/student/all-studyplans";
    }

    @ModelAttribute("studyPlans")
    private Iterable<StudyPlan> getStudyPlans() {
        return studyPlanService.findAll();
    }
}
