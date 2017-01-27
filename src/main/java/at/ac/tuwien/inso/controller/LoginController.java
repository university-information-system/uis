package at.ac.tuwien.inso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

	@GetMapping
	public String getLogin(
			@RequestParam(value = "loggedOut", required = false) String loggedOut,
			@RequestParam(value = "error", required = false) String error,
			HttpSession session,
            Model model,
			RedirectAttributes redirectAttributes
	) {
	    // Show a flash messages after a logout
		if (loggedOut != null) {
			redirectAttributes.addFlashAttribute("flashMessage", "login.loggedOut");
			return "redirect:/login";
		}

		// TODO: check for previous page

		// Show a message after an error
		if (error != null) {
            Exception lastException = (Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

            if (lastException != null) {
                redirectAttributes.addFlashAttribute("loginError", lastException.getLocalizedMessage());
            }

			return "redirect:/login";
		}

		return "login";
	}
}
