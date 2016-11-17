package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.service.StudyPlanService;
import at.ac.tuwien.inso.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/admin/studyplans")
public class AdminStudyPlansController {

    @Autowired
    private StudyPlanService studyPlanService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String studyplans() {
        return "admin/studyplans";
    }

    @ModelAttribute("studyPlans")
    private Iterable<StudyPlan> getStudyPlans() {
        return studyPlanService.getAllStudyPlans();
    }

    @GetMapping(params = "id")
    private String getStudyPlan(@RequestParam(value = "id") Long id, Model model) {
        StudyPlan studyPlan = studyPlanService.getStudyPlanById(id);
        model.addAttribute("studyPlan", studyPlan);
        Iterable<SubjectForStudyPlan> subjectsForStudyPlan = studyPlanService.getSubjectsForStudyPlan(id);
        model.addAttribute("mandatory", StreamSupport.stream(subjectsForStudyPlan.spliterator(), false).filter(SubjectForStudyPlan::getMandatory).collect(Collectors.toList()));
        model.addAttribute("optional", StreamSupport.stream(subjectsForStudyPlan.spliterator(), false).filter(s -> !s.getMandatory()).collect(Collectors.toList()));
        return "admin/studyplan-details";
    }
}
