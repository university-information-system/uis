package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.LecturerService;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.UisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    @Autowired
    private UisUserService uisUserService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LecturerService lecturerService;

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", uisUserService.findAll());

        return "admin/users";
    }

    @GetMapping("/{userId}")
    public String userDetails(@PathVariable Long userId, Model model) {
        UisUser user = uisUserService.findOne(userId);
        model.addAttribute("user", user);

        if (user instanceof Student) {
            Iterable<StudyPlanRegistration> studyplans = studentService.findStudyPlanRegistrationsFor((Student) user);
            model.addAttribute("studyplans", studyplans);

            return "admin/student-details";
        }

        Iterable<Subject> subjects = lecturerService.findSubjectsFor((Lecturer) user);
        model.addAttribute("subjects", subjects);

        return "admin/lecturer-details";
    }
}
