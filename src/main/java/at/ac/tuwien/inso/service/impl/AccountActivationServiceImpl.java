package at.ac.tuwien.inso.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.PendingAccountActivation;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.repository.PendingAccountActivationRepository;
import at.ac.tuwien.inso.service.AccountActivationService;

@Service
public class AccountActivationServiceImpl implements AccountActivationService {

	private static final Logger log = LoggerFactory.getLogger(AccountActivationServiceImpl.class);
	
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
