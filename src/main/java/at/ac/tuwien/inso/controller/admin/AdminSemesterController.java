package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.CreateSemesterForm;
import at.ac.tuwien.inso.controller.admin.forms.CreateUserForm;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import javax.validation.Valid;

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

    @GetMapping("/create")
    public String createSemesterView(CreateSemesterForm createSemesterForm) {
        return "admin/create-semester";
    }

    @PostMapping("/create")
    public String createSemester(@Valid CreateSemesterForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/create-semester";
        }

        Semester submittedSemester = form.toSemester();

        System.out.println(submittedSemester);

        semesterService.create(submittedSemester);

        return "redirect:/admin/semester";
    }
}
