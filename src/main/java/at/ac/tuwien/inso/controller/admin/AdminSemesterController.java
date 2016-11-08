package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/semester")
public class AdminSemesterController {

    @Autowired
    private SemesterService semesterService;

    @ModelAttribute("allSemesters")
    private Iterable<Semester> getAllSemesters() {
        return semesterService.getAllSemesters();
    }

    @ModelAttribute("currentSemester")
    private Semester getCurrentSemester() {
        return semesterService.getCurrentSemester();
    }

    @GetMapping
    public String semester() {
        return "admin/semester";
    }
}
