package at.ac.tuwien.inso.controller.admin;

import static at.ac.tuwien.inso.controller.Constants.MAX_PAGE_SIZE;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import at.ac.tuwien.inso.controller.admin.forms.AddLecturersToSubjectForm;
import at.ac.tuwien.inso.controller.admin.forms.CreateSubjectForm;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.impl.Messages;

@Controller
@RequestMapping("/admin/subjects")
public class AdminSubjectsController {

    private static final Logger logger = LoggerFactory.getLogger(AdminSubjectsController.class);

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private Messages messages;


    @GetMapping
    private String listSubjects(
            @RequestParam(value = "search", required = false) String search,
            Model model
    ) {
        if ("".equals(search)) {
            return "redirect:/admin/subjects";
        }

        return listSubjectsForPageInternal(search, 1, model);
    }

    @GetMapping("/page/{pageNumber}")
    private String listSubjectsForPage(
            @RequestParam(value = "search", required = false) String search,
            @PathVariable Integer pageNumber,
            Model model
    ) {
        if (search == null && pageNumber == 1) {
            return "redirect:/admin/subjects";
        }

        if ("".equals(search)) {
            return "redirect:/admin/subjects/page/" + pageNumber;
        }

        if (pageNumber == 1) {
            return "redirect:/admin/subjects?search=" + search;
        }

        return listSubjectsForPageInternal(search, pageNumber, model);
    }

    /**
     * Does all the work for listSubjects and listSubjectsForPage
     */
    private String listSubjectsForPageInternal(String search, int pageNumber, Model model) {
        if (search == null) {
            search = "";
        }

        // Page numbers in the URL start with 1
        PageRequest page = new PageRequest(pageNumber - 1, MAX_PAGE_SIZE);

        Page<Subject> subjectsPage = subjectService.findBySearch(search, page);
        List<Subject> subjects = subjectsPage.getContent();

        // If the user tries to access a page that doesn't exist
        if (subjects.size() == 0 && subjectsPage.getTotalElements() != 0) {
            int lastPage = subjectsPage.getTotalPages();
            return "redirect:/admin/subjects/page/" + lastPage + "?search=" + search;
        }

        model.addAttribute("subjects", subjects);
        model.addAttribute("page", subjectsPage);
        model.addAttribute("search", search);

        return "admin/subjects";
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
    
    @PostMapping("/remove/{id}")
    private String removeSubject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
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
