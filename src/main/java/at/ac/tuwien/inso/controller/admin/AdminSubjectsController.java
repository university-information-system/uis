package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.AddLecturersToSubjectForm;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/subjects")
public class AdminSubjectsController {

    @Autowired
    private SubjectService subjectService;

    private static final Integer SUBJECTS_PER_PAGE = 10;

    @GetMapping
    private String listSubjects() {
        return "redirect:/admin/subjects/page/0";
    }

    @GetMapping("/page/{nr}")
    private String listSubjectsForPage(@PathVariable Integer nr, Model model) {
        Page<Subject> subjects = subjectService.findAll(new PageRequest(nr, SUBJECTS_PER_PAGE));
        model.addAttribute("subjects", subjects.getContent());
        model.addAttribute("page", subjects);
        return "admin/subjects";
    }

    @GetMapping("/{id}")
    private String getSubject(
        @PathVariable Long id,
        Model model,
        AddLecturersToSubjectForm addLecturersToSubjectForm
        ) {
      Subject subject = subjectService.findOne(id);
      model.addAttribute("subject", subject);
      model.addAttribute("lecturers", subject.getLecturers());
      model.addAttribute("requiredSubjects", subject.getRequiredSubjects());
      return "admin/subject-details";
    }
    
    @GetMapping("/add")
    private String addSubject(Model model) {
    	model.addAttribute("subject", new Subject());
        return "admin/subject-add";
    }
    
    @PostMapping("/add")
    private String addSubject(@ModelAttribute Subject subject, Model model) {
        subject = subjectService.create(subject);
        
        model.addAttribute("subject", subject);
        model.addAttribute("lecturers", subject.getLecturers());
        model.addAttribute("requiredSubjects", subject.getRequiredSubjects());
    	return "admin/subject-details";
    }
    
    @GetMapping("/delete/{id}")
    private String deleteSubject(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
      //model.addAttribute("subject", new Subject());
      Subject subject = subjectService.findOne(id);  	
      if(subject == null){
        System.out.println("error1");

        model.addAttribute("message", "test");
        redirectAttributes.addFlashAttribute("error", "test2");
        redirectAttributes.addFlashAttribute("message", "test3");
        return "redirect:/error";
      }else{
        try{
          subjectService.delete(subject);
        }catch (ValidationException e) {
          //error
          System.out.println("error"+e);
          model.addAttribute("message", "test2");
          redirectAttributes.addFlashAttribute("error", "test22");
          redirectAttributes.addFlashAttribute("message", "test32");
          return "redirect:/error";
        }
      }


      return "redirect:/admin/subjects/page/1";
    }
}
