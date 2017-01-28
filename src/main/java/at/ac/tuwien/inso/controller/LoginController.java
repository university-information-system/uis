package at.ac.tuwien.inso.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import at.ac.tuwien.inso.entity.UserAccount;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@GetMapping("/login")
	public String getLogin(
            @RequestParam(value = "loggedOut", required = false) String loggedOut,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "invalidSession", required = false) String invalidSession,
            @RequestParam(value = "notLoggedIn", required = false) String notLoggedIn,
			HttpSession session,
            HttpServletRequest request,
            Model model,
			RedirectAttributes redirectAttributes
	) {

	    // Show a flash messages after a logout
		if (loggedOut != null) {
			redirectAttributes.addFlashAttribute("flashMessage", "login.loggedOut");
			return "redirect:/login";
		}

		// Show a message after an error
		if (error != null) {
            Exception lastException = (Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

            if (lastException != null) {
                redirectAttributes.addFlashAttribute("loginError", lastException.getLocalizedMessage());
            }

            log.info("Removing ?error parameter from /login");
			return "redirect:/login";
		}

		// Show a message after the session became invalid
        if (invalidSession != null) {
            redirectAttributes.addFlashAttribute("flashMessage", "login.invalidSession");
            log.info("Removing ?invalidSession parameter from /login");
            return "redirect:/login";
        }

        // Show a message, that the user has to login first
        if (notLoggedIn != null) {
            redirectAttributes.addFlashAttribute("flashMessage", "login.notLoggedIn");
            log.info("Removing ?notLoggedIn parameter from /login");
            return "redirect:/login";
        }

        // Redirect to "/" if the user is already logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object userAccount = auth.getPrincipal();

        if (userAccount != null && userAccount instanceof UserAccount) {
            UserAccount castUserAccount = (UserAccount) userAccount;
            Long userId = castUserAccount.getId();

            if (userId != null && userId > 0) {
                log.info("User " + userId + " is already logged in");
                redirectAttributes.addFlashAttribute("flashMessage", "login.already-logged-in");
                return "redirect:/";
            }
        }

        return "login";
	}
}
