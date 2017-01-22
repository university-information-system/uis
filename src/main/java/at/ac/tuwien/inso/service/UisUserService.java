package at.ac.tuwien.inso.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;

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

    /**
     * looks up if a UisUser with the provided id exists in the repository. returns a true boolean if the id exists.
     * @param id
     * @return boolean
     */
    @PreAuthorize("isAuthenticated()")
    boolean existsUserWithIdentificationNumber(String id);
}
