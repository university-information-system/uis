package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import javax.validation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    @Autowired
    private UisUserService uisUserService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private UserCreationService userCreationService;

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

    @GetMapping("/create")
    public String createUserView(CreateUserForm createUserForm) {
        return "admin/create-user";
    }

    @PostMapping("/create")
    public String createUser(@Valid CreateUserForm form,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/create-user";
        }

        PendingAccountActivation activation = userCreationService.create(form.toUisUser());

        redirectAttributes.addFlashAttribute("createdUser", activation.getForUser());

        return "redirect:/admin/users";
    }
}
