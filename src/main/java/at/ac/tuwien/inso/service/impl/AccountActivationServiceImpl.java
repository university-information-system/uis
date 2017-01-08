package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
public class AccountActivationServiceImpl implements AccountActivationService {

	private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);
	
    @Autowired
    private PendingAccountActivationRepository pendingAccountActivationRepository;

    @Autowired
    private Messages messages;

    @Override
    @Transactional(readOnly = true)
    public PendingAccountActivation findOne(String activationCode) {
    	log.info("try finding pending activation for activationcode "+activationCode);
        PendingAccountActivation pendingAccountActivation = pendingAccountActivationRepository.findOne(activationCode);

        if (pendingAccountActivation == null) {
        	log.warn("failed finding pending activaiton for activation code "+activationCode);
            throw new BusinessObjectNotFoundException(messages.msg("error.activation_code.notfound"));
        }

        return pendingAccountActivation;
    }

    @Override
    @Transactional
    public void activateAccount(String activationCode, UserAccount account) {
    	log.info("activating account with activationCode "+activationCode+" for account "+account.toString());
        PendingAccountActivation pendingAccountActivation = findOne(activationCode);

        pendingAccountActivation.getForUser().activate(account);

        pendingAccountActivationRepository.delete(pendingAccountActivation);
    }
}
