package at.ac.tuwien.inso.controller;

import at.ac.tuwien.inso.controller.forms.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import javax.servlet.http.*;
import javax.validation.*;

@Controller
@RequestMapping("/account_activation/{activationCode}")
public class AccountActivationController {

    @Autowired
    private AccountActivationService accountActivationService;

    @GetMapping
    public String accountActivationView(@PathVariable String activationCode,
                                        Model model,
                                        AccountActivationForm accountActivationForm) {
        model.addAttribute("user", accountActivationService.findOne(activationCode).getForUser());

        return "account-activation";
    }

    @PostMapping
    public String activateAccount(@PathVariable String activationCode,
                                  @Valid AccountActivationForm accountActivationForm,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes attributes,
                                  HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", accountActivationService.findOne(activationCode).getForUser());

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "account-activation";
        }

        accountActivationService.activateAccount(activationCode, accountActivationForm.toUserAccount());
        attributes.addFlashAttribute("flashMessage", "account.activated");

        return "redirect:/login";
    }
}
