package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.service.validator.UisUserValidator;
import at.ac.tuwien.inso.service.validator.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import at.ac.tuwien.inso.entity.PendingAccountActivation;
import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.repository.PendingAccountActivationRepository;
import at.ac.tuwien.inso.service.UserCreationService;

@Service
public class UserCreationServiceImpl implements UserCreationService {

    public static final String MAIL_SUBJECT = "user.account.activation.mail.subject";
    private static final String MAIL_TEMPLATE = "emails/user-account-activation-mail";
    private ValidatorFactory validatorFactory = new ValidatorFactory();
    private UisUserValidator validator = validatorFactory.getUisUserValidator();

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
    @Override
    public PendingAccountActivation create(UisUser user) {
        validator.validateNewUisUser(user);
        PendingAccountActivation activation = new PendingAccountActivation(user);

        activation = pendingAccountActivationRepository.save(activation);

        mailSender.send(createActivationMail(activation));

        return activation;
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
