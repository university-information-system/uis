package at.ac.tuwien.inso.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import at.ac.tuwien.inso.controller.forms.AccountActivationForm;
import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.service.AccountActivationService;

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
        UisUser user = accountActivationService.findOne(activationCode).getForUser();

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "account-activation";
        }

        accountActivationService.activateAccount(activationCode, accountActivationForm.toUserAccount(user));
        attributes.addFlashAttribute("flashMessage", "account.activated");

        return "redirect:/login";
    }
}
