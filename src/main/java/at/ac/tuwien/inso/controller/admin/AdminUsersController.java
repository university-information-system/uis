package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.*;
import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.web.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import java.io.UnsupportedEncodingException;

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
    public String listUsers(
            @RequestParam(value = "search", defaultValue = "") String searchFilter,
            @PageableDefault Pageable pageable,
            Model model
    ) {
        if (pageable.getPageSize() > Constants.MAX_PAGE_SIZE) {
            pageable = new PageRequest(pageable.getPageNumber(), Constants.MAX_PAGE_SIZE);
        }
        model.addAttribute("page", uisUserService.findAllMatching(searchFilter, pageable));

        return "admin/users";
    }

    @GetMapping("/{userId}")
    public String userDetails(@PathVariable Long userId, Model model) throws UnsupportedEncodingException {
        UisUser user = uisUserService.findOne(userId);
        model.addAttribute("user", user);

        if (user instanceof Student) {
            Iterable<StudyPlanRegistration> studyplans = studentService.findStudyPlanRegistrationsFor((Student) user);
            model.addAttribute("studyplans", studyplans);

            return "admin/student-details";
        }

        Iterable<Subject> subjects = lecturerService.findSubjectsFor((Lecturer) user);
        model.addAttribute("subjects", subjects);
        String qr = lecturerService.generateQRUrl((Lecturer) user);
        model.addAttribute("qr", qr);

        return "admin/lecturer-details";
    }

    @PostMapping("/create")
    public String createUser(@Valid CreateUserForm form,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("flashMessage", "admin.users.create.error." + bindingResult.getFieldError().getField());

            return "redirect:/admin/users";
        }

        userCreationService.create(form.toUisUser());

        redirectAttributes.addFlashAttribute("flashMessage", "admin.users.create.success");

        return "redirect:/admin/users";
    }
}
