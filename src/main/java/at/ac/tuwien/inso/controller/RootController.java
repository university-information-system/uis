package at.ac.tuwien.inso.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.UserAccount;

@Controller
@RequestMapping("/")
public class RootController {

    private static final Logger log = LoggerFactory.getLogger(RootController.class);


    @GetMapping
    public String getLogin(Model model, RedirectAttributes redirectAttributes) {

        // Keep flash attributes in the next view
        Map<String, Object> attributesMap = model.asMap();
        for (String modelKey : attributesMap.keySet()) {
            redirectAttributes.addFlashAttribute(modelKey, attributesMap.get(modelKey));
        }


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object userAccountObject = auth.getPrincipal();

        boolean isInstanceOfUserAccount = userAccountObject instanceof UserAccount;
        if (!isInstanceOfUserAccount) {
            log.info("Redirect from / to /login");
            return "redirect:/login";
        }

        UserAccount userAccount = (UserAccount) userAccountObject;

        if (userAccount.hasRole(Role.ADMIN)) {
            return "redirect:/admin/studyplans";
        } else if (userAccount.hasRole(Role.LECTURER)) {
            return "redirect:/lecturer/courses";
        } else {
            return "redirect:/student/courses";
        }
    }
}
