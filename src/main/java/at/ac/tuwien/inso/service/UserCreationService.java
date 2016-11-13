package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.thymeleaf.*;
import org.thymeleaf.context.*;

import javax.mail.*;
import javax.mail.internet.*;

@Service
public class UserCreationService {

    public static final String MAIL_SUBJECT = "user.account.activation.mail.subject";
    private static final String MAIL_TEMPLATE = "emails/user-account-activation-mail";
    @Autowired
    private PendingAccountActivationRepository pendingAccountActivationRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private Messages messages;

    @Value("${uis.server.account.activation.url.prefix}")
    private String activationUrlPrefix;

    @Transactional
    public PendingAccountActivation create(UisUser user) {
        PendingAccountActivation activation = new PendingAccountActivation(user);

        mailSender.send(createActivationMail(activation));

        return pendingAccountActivationRepository.save(activation);
    }

    private MimeMessage createActivationMail(PendingAccountActivation activation) {
        MimeMessage mimeMsg = mailSender.createMimeMessage();
        MimeMessageHelper msg = new MimeMessageHelper(mimeMsg, "UTF-8");

        try {
            msg.setTo(activation.getForUser().getEmail());
            msg.setSubject(messages.get(MAIL_SUBJECT));
            msg.setText(messageContent(activation), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return msg.getMimeMessage();
    }

    private String messageContent(PendingAccountActivation activation) {
        Context context = new Context(Messages.LOCALE);
        context.setVariable("user", activation.getForUser());
        context.setVariable("activationUrl", activationUrlPrefix + activation.getId());

        return templateEngine.process(MAIL_TEMPLATE, context);
    }
}
