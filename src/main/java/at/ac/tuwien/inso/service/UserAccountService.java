package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.*;

public interface UserAccountService extends UserDetailsService {

	/**
	 * can only be used by authenticated users. This method returns the currently logged in {@link UserAccount}
	 * @return
	 */
    @PreAuthorize("isAuthenticated()")
    UserAccount getCurrentLoggedInUser();

    /**
     * returns true of the username exists. false otherwise.
     * @param username
     * @return a boolean true of the username exists.
     */
    boolean existsUsername(String username);
}
