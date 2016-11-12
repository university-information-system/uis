package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
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
    private Iterable<Subject> getSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping(params = "id")
    private String getSubject(@RequestParam(value = "id") Long id, Model model) {
        Subject subject = subjectService.getSubjectById(id);
        model.addAttribute("subject", subject);
        model.addAttribute("lecturers", subject.getLecturers());
        model.addAttribute("requiredSubjects", subject.getRequiredSubjects());
        return "admin/subject-details";
    }
    
    @GetMapping("/add")
    private String addSubject(Model model) {
        /*Subject subject = subjectService.getSubjectById(id);
        model.addAttribute("subject", subject);
        model.addAttribute("lecturers", subject.getLecturers());
        model.addAttribute("requiredSubjects", subject.getRequiredSubjects());*/
    	model.addAttribute("subject", subjectService.getNewSubject());
        return "admin/subject-add";
    }
    
    @PostMapping("/add")
    private String addSubject(@ModelAttribute Subject subject) {
    	System.out.println(subject.getName());//immer null.. TODO, check warum
    	System.out.println("ects"+subject.getEcts());
    	return "admin/subject-details";
    }
}
