package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.*;

@Controller
@RequestMapping("/admin/studyplans")
public class AdminStudyPlansController {

    @Autowired
    private StudyPlanService studyPlanService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String getStudyplansView() {
        return "admin/studyplans";
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

        return "admin/studyplan-details";
    }

    @GetMapping("/create")
    public String getCreateStudyplanView(CreateStudyPlanForm form) {
        return "admin/create-studyplan";
    }

    @PostMapping("/create")
    public String createStudyPlan(@Valid CreateStudyPlanForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/create-studyplan";
        }
        model.addAttribute("studyPlan",studyPlanService.create(form.toStudyPlan()));

        return "admin/studyplan-details";
    }

    @PostMapping(value = "/addSubject", params = {"subjectId", "studyPlanId", "semester", "mandatory"})
    public String addSubjectToStudyPlan(
            RedirectAttributes redirectAttributes,
            @RequestParam Long subjectId,
            @RequestParam Long studyPlanId,
            @RequestParam Integer semester,
            @RequestParam Boolean mandatory) {

        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(studyPlanId);
        Subject subject = new Subject();
        subject.setId(subjectId);
        try {
            studyPlanService.addSubjectToStudyPlan(new SubjectForStudyPlan(subject, studyPlan, mandatory, semester));
        }
        catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/studyplans/?id=" + studyPlanId;
    }
    
    @GetMapping(value = "/disable", params = {"id"})
    private String disableStudyPlan(@RequestParam(value = "id") Long id, Model model) {
      System.out.println("Disabling "+id);
      studyPlanService.disableStudyPlan(id);
      return "redirect:/admin/studyplans";
    }


    @GetMapping(value = "/json/availableSubjects", params = {"id", "query"})
    @ResponseBody
    public List<Subject> getAvailableSubjects(@RequestParam Long id, @RequestParam String query) {
        return studyPlanService.getAvailableSubjectsForStudyPlan(id, query);
    }

}
