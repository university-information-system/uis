package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.service.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping(params = "id")
    private String getStudyplanDetailsView(@RequestParam(value = "id") Long id, Model model) {
        StudyPlan studyPlan = studyPlanService.findOne(id);

        List<SubjectForStudyPlan> subjectsForStudyPlan = studyPlanService.getSubjectsForStudyPlan(id);
        List<SubjectForStudyPlan> mandatory = subjectsForStudyPlan
                .stream()
                .filter(SubjectForStudyPlan::getMandatory)
                .collect(Collectors.toList());
        List<SubjectForStudyPlan> optional = subjectsForStudyPlan
                .stream()
                .filter(s -> !s.getMandatory())
                .collect(Collectors.toList());

        model.addAttribute("studyPlan", studyPlan);
        model.addAttribute("mandatory", mandatory);
        model.addAttribute("optional", optional);

        return "student/all-studyplan-details";
    }
}
