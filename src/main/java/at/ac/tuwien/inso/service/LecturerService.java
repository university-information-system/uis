package at.ac.tuwien.inso.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Subject;

public interface LecturerService {

	/**
	 * returns the currently logged in lecturer
	 * 
	 * user needs to be lecturer
	 * 
	 * @return the logged in lecturer
	 */
    @PreAuthorize("hasRole('LECTURER')")
    Lecturer getLoggedInLecturer();

    /**
     * returns all subjects that are owned by the currently logged in lecturer
     * 
     * user needs to be lecturer
     * 
     * @return
     */
    @PreAuthorize("hasRole('LECTURER')")
    Iterable<Subject> getOwnSubjects();

    /**
     * finds subjects for the given lecturer. lecturer should not be null
     * 
     * user needs to be authenticated
     * 
     * @param lecturer
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    List<Subject> findSubjectsFor(Lecturer lecturer);

    /**
     * 
     * @param lecturer
     * @return
     * @throws UnsupportedEncodingException
     */
    @PreAuthorize("hasRole('ADMIN')")
    String generateQRUrl(Lecturer lecturer) throws UnsupportedEncodingException;
}
