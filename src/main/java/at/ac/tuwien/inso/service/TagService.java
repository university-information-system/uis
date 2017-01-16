package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface TagService {

	/**
	 * returns all tags.
	 * can only be used if a user is authenticated
	 * 
	 * @return a list of {@link Tag}s
	 */
    @PreAuthorize("isAuthenticated()")
    List<Tag> findAll();

    /**
     * returns a tag by name
     * can only be used if a user is authenticated
     * @param name
     * @return a {@link Tag} object
     */
    @PreAuthorize("isAuthenticated()")
    Tag findByName(String name);
}
