package at.ac.tuwien.inso.controller;

import at.ac.tuwien.inso.controller.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account_activation/{activationCode}")
public class AccountActivationController {

    @Autowired
    private AccountActivationService accountActivationService;

    @GetMapping
    public String accountActivationView(@PathVariable String activationCode,
                                        Model model,
                                        AccountActivationForm accountActivationForm) {
        PendingAccountActivation pendingAccountActivation = accountActivationService.findOne(activationCode);
        model.addAttribute("user", pendingAccountActivation.getForUser());

        return "account-activation";
    }

    @PostMapping
    public String activateAccount(AccountActivationForm accountActivationForm) {
        return "redirect:/login";
    }
}
