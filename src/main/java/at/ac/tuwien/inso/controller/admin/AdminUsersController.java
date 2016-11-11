package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@ModelAttribute("form") CreateUserForm form, Model model) {
        PendingAccountActivation activation = userCreationService.create(form.toUisUser());

        model.addAttribute("createdUser", activation.getForUser());

        return "/admin/users";
    }
}
