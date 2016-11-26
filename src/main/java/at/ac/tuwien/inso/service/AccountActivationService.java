package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

public interface AccountActivationService {

    PendingAccountActivation findOne(String activationCode);

    void activateAccount(String activationCode, UserAccount account);
}
