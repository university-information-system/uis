package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

public interface UserCreationService {

    @PreAuthorize("hasRole('ADMIN')")
    PendingAccountActivation create(UisUser user);
}
