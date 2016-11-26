package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
public class AccountActivationServiceImpl implements AccountActivationService {

    @Autowired
    private PendingAccountActivationRepository pendingAccountActivationRepository;

    @Autowired
    private Messages messages;

    @Override
    @Transactional(readOnly = true)
    public PendingAccountActivation findOne(String activationCode) {
        PendingAccountActivation pendingAccountActivation = pendingAccountActivationRepository.findOne(activationCode);

        if (pendingAccountActivation == null) {
            throw new BusinessObjectNotFoundException(messages.msg("error.activation_code.notfound"));
        }

        return pendingAccountActivation;
    }

    @Override
    @Transactional
    public void activateAccount(String activationCode, UserAccount account) {
        PendingAccountActivation pendingAccountActivation = findOne(activationCode);

        pendingAccountActivation.getForUser().activate(account);

        pendingAccountActivationRepository.delete(pendingAccountActivation);
    }
}
