package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;

import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.*;

public interface UisUserService {

	/**
	 * returns all matching UisUsers that fit the searchFilter and are on the provided Pageable.
	 * the query should be ordered desc. by the user IDs
	 * can only be used by ADMINs.
	 *
	 * 
	 * @param searchFilter
	 * @param pageable
	 * @return
	 */
    @PreAuthorize("hasRole('ADMIN')")
    Page<UisUser> findAllMatching(String searchFilter, Pageable pageable);

    /**
     * 
     * @param long id. should not be null and not <1 
     * @return UisUser
     * @throws BusinessObjectNotFoundException
     * 
     * returns an {@link UisUser} with the provided id. if no user can be found
     * can only be used by ADMINs. if no user is found a {@link BusinessObjectNotFoundException} will be thrown
     */
    @PreAuthorize("hasRole('ADMIN')")
    UisUser findOne(long id) throws BusinessObjectNotFoundException;

    @PreAuthorize("isAuthenticated()")
    boolean existsUserWithIdentificationNumber(String id);
}
