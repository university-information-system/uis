package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/subjects")
public class AdminSubjectsController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String subjects() {
        return "admin/subjects";
    }

    @ModelAttribute("subjects")
    private Iterable<Subject> getOwnSubjects() {
        return subjectService.getAllSubjects();
    }
}
