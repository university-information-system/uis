package at.ac.tuwien.inso.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.UserAccount;

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
