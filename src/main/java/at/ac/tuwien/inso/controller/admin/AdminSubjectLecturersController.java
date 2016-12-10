package at.ac.tuwien.inso.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import javax.validation.Valid;

import at.ac.tuwien.inso.controller.admin.forms.AddLecturersToSubjectForm;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.SubjectService;

@Controller
@RequestMapping("/admin/subjects/{subjectId}")
public class AdminSubjectLecturersController {

    @Autowired
    private SubjectService subjectService;

    @ModelAttribute("subject")
    private Subject getSubject(@PathVariable Long subjectId) {
        return subjectService.findOne(subjectId);
    }

    @GetMapping(value = "/availableLecturers.json")
    @ResponseBody
    public List<Lecturer> getAvailableLecturers(@PathVariable Long subjectId) {
        return subjectService.getAvailableLecturersForSubject(subjectId);
    }

    @PostMapping("/lecturers")
    public String addLecturer(
            @PathVariable Long subjectId,
            @Valid AddLecturersToSubjectForm addLecturersToSubjectForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        Long lecturerUisUserId = addLecturersToSubjectForm.toLecturerId();
        subjectService.addLecturerToSubject(subjectId, lecturerUisUserId);

        return "redirect:/admin/subjects/" + subjectId;
    }

}
