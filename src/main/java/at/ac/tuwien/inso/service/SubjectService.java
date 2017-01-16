package at.ac.tuwien.inso.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface SubjectService {

	/**
	 * returns all Subjects that match the search text.
	 * 
	 * only works if user is authenticated.
	 * @param search
	 * @param pageable
	 * @return
	 */
    @PreAuthorize("isAuthenticated()")
    Page<Subject> findBySearch(String search, Pageable pageable);

    /**
     * returns a Subject by id.
     * 
     * @param Long id, should not be null and not <1
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    Subject findOne(Long id);

    /**
     * returns a list of subjects that fit the search word. The filter has to be findByNameContainingIgnoreCase
     * 
     * @param word
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    List<Subject> searchForSubjects(String word);

    @PreAuthorize("hasRole('ADMIN')")
    Subject create(Subject subject);

    @PreAuthorize("hasRole('ADMIN')")
    Lecturer addLecturerToSubject(Long subjectId, Long lecturerUisUserId);

    @PreAuthorize("hasRole('ADMIN')")
    List<Lecturer> getAvailableLecturersForSubject(Long subjectId, String search);

    /**
     * Removes a lecturer from a subject
     *
     *only works if the user is ADMIN
     *
     * @param subjectId
     * @param lecturerUisUserId
     * @return the removed lecturer
     */
    @PreAuthorize("hasRole('ADMIN')")
    Lecturer removeLecturerFromSubject(Long subjectId, Long lecturerUisUserId);

    /**
     * removes a subject from the system. is only possible if there exist no courses for this subject.
     * if courses exist, a validation exception will be thrown.
     * 
     * only works if the user is ADMIN
     * 
     * @param subject
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
	boolean remove(Subject subject);
}
