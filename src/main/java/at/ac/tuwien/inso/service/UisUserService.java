package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.*;

public interface UisUserService {

    @PreAuthorize("hasRole('ADMIN')")
    Page<UisUser> findAllMatching(String searchFilter, Pageable pageable);

    @PreAuthorize("hasRole('ADMIN')")
    UisUser findOne(long id);

    @PreAuthorize("isAuthenticated()")
    boolean existsUserWithIdentificationNumber(String id);
}
