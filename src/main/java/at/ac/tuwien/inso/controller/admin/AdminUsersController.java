package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    @Autowired
    private UisUserService uisUserService;

    @Autowired
    private UserCreationService userCreationService;

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", uisUserService.findAll());

        return "admin/users";
    }

    @GetMapping("/create")
    public String createUserView() {
        return "admin/create-user";
    }

    @PostMapping
    public String createUser(@ModelAttribute("form") CreateUserForm form, RedirectAttributes redirectAttributes) {
        PendingAccountActivation activation = userCreationService.create(form.toUisUser());

        redirectAttributes.addFlashAttribute("createdUser", activation.getForUser());

        return "redirect:/admin/users";
    }
}
