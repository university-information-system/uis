package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

public interface AccountActivationService {

	/**
	 * tries to return the PendingAccountActivation Object that is linked to the parameter activationCode. If there is no object found a BusinessObjectNotFoundException will be thrown.
	 * @param activationCode
	 * @return a found PendingAccountActivation object or a BusinessObjectNotFoundException
	 */
    PendingAccountActivation findOne(String activationCode);

    /**
     * 
     * activates the account that is identified by activationCode and sets the UserAccount to it.
     * 
     * @param activationCode a previously created activation code
     * @param account an account to activate with the activation code
     */
    void activateAccount(String activationCode, UserAccount account);
}
