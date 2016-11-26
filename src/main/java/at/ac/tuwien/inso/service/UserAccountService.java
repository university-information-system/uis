package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.*;

public interface UserAccountService extends UserDetailsService {

    @PreAuthorize("isAuthenticated()")
    UserAccount getCurrentLoggedInUser();

    boolean existsUsername(String username);
}
