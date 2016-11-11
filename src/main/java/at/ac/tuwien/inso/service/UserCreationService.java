package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.thymeleaf.*;
import org.thymeleaf.context.*;

@Service
public class UserCreationService {

    public static final String MAIL_SUBJECT = "user.account.activation.mail.subject";
    private static final String MAIL_TEMPLATE = "emails/user-account-activation-mail";
    @Autowired
    private PendingAccountActivationRepository pendingAccountActivationRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private Messages messages;

    @Value("${uis.server.account.activation.url.prefix}")
    private String activationUrlPrefix;

    @Transactional
    public PendingAccountActivation create(UisUser user) {
        PendingAccountActivation activation = new PendingAccountActivation(user);

        sendActivationMail(activation);

        return pendingAccountActivationRepository.save(activation);
    }

    private void sendActivationMail(PendingAccountActivation activation) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(activation.getForUser().getEmail());
        msg.setSubject(messages.get(MAIL_SUBJECT));
        msg.setText(messageContent(activation));

        mailSender.send(msg);
    }

    private String messageContent(PendingAccountActivation activation) {
        Context context = new Context(Messages.LOCALE);
        context.setVariable("user", activation.getForUser());
        context.setVariable("activationUrl", activationUrlPrefix + activation.getId());

        return templateEngine.process(MAIL_TEMPLATE, context);
    }
}
