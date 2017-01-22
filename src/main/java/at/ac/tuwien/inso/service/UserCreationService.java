package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.PendingAccountActivation;
import at.ac.tuwien.inso.entity.UisUser;

public interface UserCreationService {

	/**
	 * this methods creates a new {@link UisUser} that should not automatically be activated by this method.
	 *
	 * 
	 * Can only be used by ADMINS.
	 * May have extended behavior e.g. sending activation mails.
	 * May throw a runtime exception if any steps fail.
	 * 
	 * @param user should not be null, should have a not empty name, identificationNumber and mail address
	 * @return a {@link PendingAccountActivation} that contains the new {@link UisUser} and an activation id.
	 */
    @PreAuthorize("hasRole('ADMIN')")
    PendingAccountActivation create(UisUser user);
}
