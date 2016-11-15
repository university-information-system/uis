package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.service.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/studyplans")
public class AdminStudyPlansController {

    @Autowired
    private StudyPlanService studyPlanService;

    @GetMapping
    public String studyplans() {
        return "admin/studyplans";
    }

    @ModelAttribute("studyPlans")
    private Iterable<StudyPlan> getStudyPlans() {
        return studyPlanService.getAllStudyPlans();
    }
}
