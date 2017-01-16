package at.ac.tuwien.inso.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


	private static final Logger log = LoggerFactory.getLogger(UserCreationServiceImpl.class);
		
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

    
    //for doc see interface
    @Transactional
    @Override
    public PendingAccountActivation create(UisUser user) {
    	log.info("creating pendingaccountactivation by user "+user.toString());
   
        validator.validateNewUisUser(user);
        PendingAccountActivation activation = new PendingAccountActivation(user);

        activation = pendingAccountActivationRepository.save(activation);

        log.info("sending activation mail now!");
        mailSender.send(createActivationMail(activation));

        return activation;
    }

    /**
     * creates a activation mail.
     * 
     * may throw a RuntimeException if no MimeMessage can be created
     * 
     * @param activation {@link PendingAccountActivation}  has to contain an id and an UisUser. UisUser should contain an mail address. 
     * @return {@link MimeMessage}
     */
    private MimeMessage createActivationMail(PendingAccountActivation activation) throws RuntimeException{
    	log.info("creating activation mail for pending activation");
        MimeMessage mimeMsg = mailSender.createMimeMessage();
        MimeMessageHelper msg = new MimeMessageHelper(mimeMsg, "UTF-8");

        try {
            msg.setTo(activation.getForUser().getEmail());
            msg.setSubject(messages.get(MAIL_SUBJECT));
            msg.setText(messageContent(activation), true);
        } catch (MessagingException e) {
        	log.warn("activation message creation FAILED! throwing runtime-exception now");
            throw new RuntimeException(e);
        }

        return msg.getMimeMessage();
    }

    /**
     * Creates a String that represents the message content of the mail.
     * 
     * @param activation {@Link PendingAccountActivation} has to contain an id and an UisUser
     * @return 
     */
    private String messageContent(PendingAccountActivation activation) {
    	log.info("getting message context for PendingAccountActivation "+activation);
        Context context = new Context(Messages.LOCALE);
        context.setVariable("user", activation.getForUser());
        context.setVariable("activationUrl", activationUrlPrefix + activation.getId());
        
        return templateEngine.process(MAIL_TEMPLATE, context);
    }
}
