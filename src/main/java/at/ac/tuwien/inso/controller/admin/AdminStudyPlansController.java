package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.CreateStudyPlanForm;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.service.StudyPlanService;
import at.ac.tuwien.inso.service.SubjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        return studyPlanService.getAllStudyPlans();
    }

    @GetMapping(params = "id")
    private String getStudyPlan(@RequestParam(value = "id") Long id, Model model) {
        StudyPlan studyPlan = studyPlanService.getStudyPlanById(id);
        model.addAttribute("studyPlan", studyPlan);
        this.subjectsForStudyPlan = StreamSupport.stream(studyPlanService.getSubjectsForStudyPlan(id).spliterator(), false).collect(Collectors.toList());
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

    @PostMapping(value = "/addSubject", params = {"subjectJson", "studyPlanId", "semester"})
    public String addSubjectToStudyPlan(
            @RequestParam String subjectJson,
            @RequestParam Long studyPlanId,
            @RequestParam Integer semester,
            @RequestParam Boolean mandatory) {

        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(studyPlanId);
        try {
            Subject subject = new ObjectMapper().readValue(subjectJson, Subject.class);
            studyPlanService.addSubjectToStudyPlan(new SubjectForStudyPlan(subject, studyPlan, mandatory, semester));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/studyplans/?id=" + studyPlanId;
    }


    @GetMapping(value = "/json/availableSubjects", params = "query")
    @ResponseBody
    public List<Subject> subjects(@RequestParam String query) {
        List<Subject> subjects = new ArrayList<>();
        Iterable<Subject> iterable = subjectService.searchForSubjects(query);
        Iterator<Subject> iter = iterable.iterator();
        while(iter.hasNext()) {
            Subject subject = iter.next();
            if(!usedSubjects.contains(subject)) {
                subjects.add(subject);
            }
        }
        return subjects;
    }

}
