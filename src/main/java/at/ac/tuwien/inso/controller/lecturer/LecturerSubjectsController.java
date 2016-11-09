package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lecturer/subjects")
public class LecturerSubjectsController {

    @Autowired
    private LecturerService lecturerService;

    @GetMapping
    public String subjects() {
        return "lecturer/subjects";
    }

    @ModelAttribute("ownedSubjects")
    private Iterable<Subject> getOwnSubjects() {
        return lecturerService.getOwnSubjects();
    }

}