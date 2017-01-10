package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.CreateSubjectForm;
import at.ac.tuwien.inso.service.impl.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import at.ac.tuwien.inso.controller.admin.forms.AddLecturersToSubjectForm;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.service.SubjectService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/subjects")
public class AdminSubjectsController {

    private static final Logger logger = LoggerFactory.getLogger(AdminSubjectsController.class);

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private Messages messages;

    private static final Integer SUBJECTS_PER_PAGE = 10;

    @GetMapping
    private String listSubjects(
            @RequestParam(value = "search", required = false) String search,
            Model model
    ) {
        return listSubjectsForPage(search, 0, model);
    }

    @GetMapping("/page/{pageNumber}")
    private String listSubjectsForPage(
            @RequestParam(value = "search", required = false) String search,
            @PathVariable Integer pageNumber,
            Model model
    ) {

        final String searchString = getSearchString(search);
        PageRequest page = new PageRequest(pageNumber, SUBJECTS_PER_PAGE);

        Page<Subject> subjects = subjectService.findBySearch(searchString, page);

        model.addAttribute("subjects", subjects.getContent());
        model.addAttribute("page", subjects);

        return "admin/subjects";
    }

    @ModelAttribute("search")
    private String getSearchString(@RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return search;
        }

        return "";
    }


    @GetMapping("/{id}")
    private String getSubject(
            @PathVariable Long id,
            Model model,
            AddLecturersToSubjectForm addLecturersToSubjectForm,
            RedirectAttributes redirectAttributes
    ) {
        Subject subject = subjectService.findOne(id);
        model.addAttribute("subject", subject);

        if (subject == null) {
            logger.info("/admin/subjects: Subject with id " + id + " not found");

            String msgId = "admin.subjects.notFound";
            redirectAttributes.addFlashAttribute("flashMessage", msgId);

            return "redirect:/admin/subjects";
        }

        model.addAttribute("lecturers", subject.getLecturers());
        model.addAttribute("requiredSubjects", subject.getRequiredSubjects());
        return "admin/subject-details";
    }

    @PostMapping("/create")
    private String createSubject(@Valid CreateSubjectForm form,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("flashMessage", "admin.subjects.create.error");
            return "redirect:/admin/subjects";
        }
        Subject subject = subjectService.create(form.toSubject());
        redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("admin.subjects.create.success", subject.getName()));

        return "redirect:/admin/subjects/" + subject.getId();
    }
    
    @GetMapping("/remove/{id}")
    private String removeSubject(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
      Subject subject = subjectService.findOne(id);  	
      if(subject == null){
    	  redirectAttributes.addFlashAttribute("flashMessage", "admin.subjects.remove.error.nosubjectfound");
          return "redirect:/admin/subjects";
      }else{
        try{
          subjectService.remove(subject);
        }catch (ValidationException e) {
          redirectAttributes.addFlashAttribute("flashMessage", "admin.subjects.remove.error.courseexists");
          return "redirect:/admin/subjects";
        }
      }

        redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("admin.subjects.remove.success", subject.getName()));
        return "redirect:/admin/subjects/page/0";
    }
}
