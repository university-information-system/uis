package at.ac.tuwien.inso.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import at.ac.tuwien.inso.controller.admin.forms.CreateStudyPlanForm;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.StudyPlanService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.impl.Messages;

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
    private Messages messages;

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
        double addedMandatoryEcts = mandatory.stream().mapToDouble(s -> s.getSubject().getEcts().doubleValue()).sum();
        double addedOptionalEcts = optional.stream().mapToDouble(s -> s.getSubject().getEcts().doubleValue()).sum();

        model.addAttribute("studyPlan", studyPlan);
        model.addAttribute("mandatory", mandatory);
        model.addAttribute("addedMandatoryEcts", BigDecimal.valueOf(addedMandatoryEcts).setScale(2));
        model.addAttribute("optional", optional);
        model.addAttribute("addedOptionalEcts", new BigDecimal(addedOptionalEcts).setScale(2));

        return "admin/studyplan-details";
    }

    @PostMapping(value = "/disable", params = {"id"})
    public String disableStudyPlan(@RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes) {
        StudyPlan studyPlan = studyPlanService.disableStudyPlan(id);
        redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("admin.studyplans.disable.success", studyPlan.getName()));
        return "redirect:/admin/studyplans";
    }

    @PostMapping("/create")
    public String createStudyPlan(@Valid CreateStudyPlanForm form,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes
                                  ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("flashMessage", "admin.studyplans.create.error");
            return "redirect:/admin/studyplans";
        }
        try {
            StudyPlan studyPlan = studyPlanService.create(form.toStudyPlan());
            model.addAttribute("studyPlan", studyPlan);
            model.addAttribute("flashMessageNotLocalized", messages.msg("admin.studyplans.create.success", studyPlan.getName()));
        } catch(DataAccessException e) {
            redirectAttributes.addFlashAttribute("flashMessage", "admin.studyplans.create.error");
            return "redirect:/admin/studyplans";
        }


        return "admin/studyplan-details";
    }

    @PostMapping(value = "/addSubject", params = {"subjectId", "studyPlanId", "semester", "mandatory"})
    public String addSubjectToStudyPlan(RedirectAttributes redirectAttributes,
                                        @RequestParam Long subjectId,
                                        @RequestParam Long studyPlanId,
                                        @RequestParam Integer semester,
                                        @RequestParam Boolean mandatory) {

        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(studyPlanId);
        try {
            Subject subject = subjectService.findOne(subjectId);
            studyPlanService.addSubjectToStudyPlan(new SubjectForStudyPlan(subject, studyPlan, mandatory, semester));
            String successMsg = messages.msg("admin.studyplans.details.subject.add.success", subject.getName());
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", successMsg);
        }
        catch (ValidationException | BusinessObjectNotFoundException e) {
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("error.subject.missing"));
        }

        return "redirect:/admin/studyplans/?id=" + studyPlanId;
    }

    @PostMapping(value = "/remove", params = {"studyPlanId", "subjectId"})
    public String removeSubjectFromStudyPlan(RedirectAttributes redirectAttributes,
                                             @RequestParam Long studyPlanId,
                                             @RequestParam Long subjectId){
        StudyPlan studyPlan = studyPlanService.findOne(studyPlanId);
        Subject subject = subjectService.findOne(subjectId);
        studyPlanService.removeSubjectFromStudyPlan(studyPlan, subject);
        String successMsg = messages.msg("admin.studyplans.details.subject.remove.success", subject.getName());
        redirectAttributes.addFlashAttribute("flashMessageNotLocalized", successMsg);
        return "redirect:/admin/studyplans/?id=" + studyPlanId;
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
    @PostMapping(value = "/registerStudent", params = "studentId")
    public String registerStudent(RedirectAttributes redirectAttributes,
                                  @RequestParam Long studentId,
                                  @RequestParam Long studyPlanId) {
        StudyPlan studyPlan = studyPlanService.findOne(studyPlanId);
        Student student = studentService.findOne(studentId);
        studentService.registerStudentToStudyPlan(student, studyPlan);
        redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("admin.student.register.success", studyPlan.getName()));

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

        return "admin/add-study-plan-to-student";
    }



}
