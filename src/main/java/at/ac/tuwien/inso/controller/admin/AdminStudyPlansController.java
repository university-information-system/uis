package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;
import java.util.stream.*;

@Controller
@RequestMapping("/admin/studyplans")
public class AdminStudyPlansController {

    @Autowired
    private StudyPlanService studyPlanService;

    @Autowired
    private SubjectService subjectService;

    private List<SubjectForStudyPlan> subjectsForStudyPlan;
    private List<Subject> usedSubjects;

    @GetMapping
    public String studyplans() {
        return "admin/studyplans";
    }

    @ModelAttribute("studyPlans")
    private Iterable<StudyPlan> getStudyPlans() {
        return studyPlanService.findAll();
    }

    @GetMapping(params = "id")
    private String getStudyPlan(@RequestParam(value = "id") Long id, Model model) {
        StudyPlan studyPlan = studyPlanService.findOne(id);
        model.addAttribute("studyPlan", studyPlan);

        Spliterator<SubjectForStudyPlan> studyplans = studyPlanService.getSubjectsForStudyPlan(id).spliterator();
        this.subjectsForStudyPlan = StreamSupport.stream(studyplans, false).collect(Collectors.toList());
        model.addAttribute("mandatory", subjectsForStudyPlan.stream().filter(SubjectForStudyPlan::getMandatory).collect(Collectors.toList()));
        model.addAttribute("optional", subjectsForStudyPlan.stream().filter(s -> !s.getMandatory()).collect(Collectors.toList()));

        //collect the subject objects for further use
        this.usedSubjects = subjectsForStudyPlan.stream().map(SubjectForStudyPlan::getSubject).collect(Collectors.toList());
        return "admin/studyplan-details";
    }

    @GetMapping("/create")
    public String createStudyPlanView(CreateStudyPlanForm form, Model model) {
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
            @RequestParam Long subjectId,
            @RequestParam Long studyPlanId,
            @RequestParam Integer semester,
            @RequestParam Boolean mandatory) {

        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(studyPlanId);
        Subject subject = new Subject();
        subject.setId(subjectId);
        studyPlanService.addSubjectToStudyPlan(new SubjectForStudyPlan(subject, studyPlan, mandatory, semester));
        return "redirect:/admin/studyplans/?id=" + studyPlanId;
    }


    @GetMapping(value = "/json/availableSubjects", params = "query")
    @ResponseBody
    public List<Subject> getAvailableSubjects(@RequestParam String query) {
        return subjectService.searchForSubjects(query)
                .stream()
                .filter(it -> !usedSubjects.contains(it))
                .collect(Collectors.toList());
    }

}
