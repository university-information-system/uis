package at.ac.tuwien.inso.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import at.ac.tuwien.inso.controller.admin.forms.AddLecturersToSubjectForm;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.SubjectService;

@Controller
@RequestMapping("/admin/subjects")
public class AdminSubjectsController {

    private static final Logger logger = LoggerFactory.getLogger(AdminSubjectsController.class);

    @Autowired
    private SubjectService subjectService;

    private static final Integer SUBJECTS_PER_PAGE = 10;

    @GetMapping
    private String listSubjects(Model model) {
        return listSubjectsForPage(0, model);
    }

    @GetMapping("/page/{pageNumber}")
    private String listSubjectsForPage(@PathVariable Integer pageNumber, Model model) {
        Page<Subject> subjects = subjectService.findAll(new PageRequest(pageNumber, SUBJECTS_PER_PAGE));
        model.addAttribute("subjects", subjects.getContent());
        model.addAttribute("page", subjects);
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
        model.addAttribute("requiredSubjects", subject.getRequiredSubjects());
        return "admin/subject-details";
    }

    @GetMapping("/add")
    private String addSubject(Model model) {
        model.addAttribute("subject", new Subject());
        return "admin/subject-add";
    }

    @PostMapping("/add")
    private String addSubject(@ModelAttribute Subject subject) {
        subject = subjectService.create(subject);
        return "admin/subject-details";
    }
}
