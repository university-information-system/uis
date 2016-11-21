package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private List<Subject> getSubjects() {
        return subjectService.findAll();
    }

    @GetMapping(params = "id")
    private String getSubject(@RequestParam(value = "id") Long id, Model model) {
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
    private String addSubject(@ModelAttribute Subject subject) {
        subject = subjectService.create(subject);
    	return "admin/subject-details";
    }
}
