package at.ac.tuwien.inso.controller;

import at.ac.tuwien.inso.entity.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping
    public String getLogin(Authentication auth) {
        UserAccount userAccount = (UserAccount) auth.getPrincipal();

        if (userAccount.hasRole(Role.ADMIN)) {
            return "redirect:/admin/studyplans";
        } else if (userAccount.hasRole(Role.LECTURER)) {
            return "redirect:/lecturer/courses";
        } else {
            return "redirect:/student/courses";
        }
    }
}
