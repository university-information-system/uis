package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import javax.validation.*;

@Controller
@RequestMapping("/admin/semester")
public class AdminSemesterController {

    @Autowired
    private SemesterService semesterService;

    @ModelAttribute("allSemesters")
    private Iterable<SemesterDto> getAllSemesters() {
        return semesterService.findAll();
    }

    @ModelAttribute("currentSemester")
    private SemesterDto getCurrentSemester() {
        return semesterService.getCurrentSemester();
    }

    @GetMapping
    public String semester() {
        return "admin/semester";
    }
}
