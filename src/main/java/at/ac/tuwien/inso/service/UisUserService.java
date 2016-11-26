package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface UisUserService {

    @PreAuthorize("hasRole('ADMIN')")
    List<UisUser> findAllMatching(String searchFilter);

    @PreAuthorize("hasRole('ADMIN')")
    UisUser findOne(long id);

    @PreAuthorize("isAuthenticated()")
    boolean existsUserWithIdentificationNumber(String id);
}
