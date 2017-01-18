package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/semester")
public class AdminSemesterController {

    @Autowired
    private SemesterService semesterService;

    @GetMapping
    public String semester(Model model) {
        model.addAttribute("currentSemester", semesterService.getOrCreateCurrentSemester());
        model.addAttribute("allSemesters", semesterService.findAll());
        return "admin/semester";
    }
}
