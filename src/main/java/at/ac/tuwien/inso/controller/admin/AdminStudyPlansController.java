package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;
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
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

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
    public String disableStudyPlan(@RequestParam(value = "id") Long id, Model model) {
      System.out.println("Disabling "+id);
      studyPlanService.disableStudyPlan(id);
      return "redirect:/admin/studyplans";
    }


    @GetMapping(value = "/json/availableSubjects", params = {"id", "query"})
    @ResponseBody
    public List<Subject> getAvailableSubjects(@RequestParam Long id, @RequestParam String query) {
        return studyPlanService.getAvailableSubjectsForStudyPlan(id, query);
    }
    
    
    /**
     * @author m.pazourek
     * @param studentId
     * @param studyPlanId
     * @return
     */
    @GetMapping(value = "/registerStudent", params = "studentId")
    public String registerStudent(@RequestParam Long studentId, @RequestParam Long studyPlanId) {
      StudyPlan studyPlan = studyPlanService.findOne(studyPlanId);
      Student student = studentService.findOne(studentId);

      studentService.registerStudentToStudyPlan(student, studyPlan);

      return "redirect:/admin/users/"+student.getId();
    }

    /**
     * @author m.pazourek
     * @param studentToAddId
     * @param model
     * @return
     */
    @GetMapping(value = "/registerStudent", params = "studentToAddId")
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

    @GetMapping(value = "/remove", params = {"studyPlanId", "subjectId"})
    public String removeSubjectFromStudyPlan(@RequestParam Long studyPlanId, @RequestParam Long subjectId, Model model){
        StudyPlan studyPlan = studyPlanService.findOne(studyPlanId);
        Subject subject = subjectService.findOne(subjectId);
        studyPlanService.removeSubjectFromStudyPlan(studyPlan, subject);
        return "redirect:/admin/studyplans/?id=" + studyPlanId;
    }

}
