package at.ac.tuwien.inso.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import at.ac.tuwien.inso.exception.LecturerNotFoundException;
import at.ac.tuwien.inso.exception.RelationNotfoundException;
import at.ac.tuwien.inso.exception.SubjectNotFoundException;
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
            RedirectAttributes redirectAttributes
    ) {

        Long lecturerUisUserId = addLecturersToSubjectForm.toLecturerId();
        subjectService.addLecturerToSubject(subjectId, lecturerUisUserId);

        return "redirect:/admin/subjects/" + subjectId;
    }

    @GetMapping("/lecturers/{lecturerId}/delete")
    public String removeLecturer(
            @PathVariable Long subjectId,
            @PathVariable Long lecturerId,
            RedirectAttributes redirectAttributes
    ) {

        try {
            Lecturer removedLecturer = subjectService.removeLecturerFromSubject(subjectId,
                    lecturerId);

            String name = removedLecturer.getName();

            redirectAttributes.addFlashAttribute("flashMessage", "Lecturer " + name + " removed");
            return "redirect:/admin/subjects/" + subjectId;

        } catch (SubjectNotFoundException e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Subject not found");
            return "redirect:/admin/subjects";
        } catch (LecturerNotFoundException e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Lecturer not found");
            return "redirect:/admin/subjects/" + subjectId;
        } catch (RelationNotfoundException e) {
            redirectAttributes.addFlashAttribute("flashMessage", "Person was not not a lecturer");
            return "redirect:/admin/subjects/" + subjectId;
        }
    }

}
