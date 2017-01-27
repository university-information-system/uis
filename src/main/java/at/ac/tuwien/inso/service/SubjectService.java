package at.ac.tuwien.inso.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Subject;

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

    /**
     * creates a new subject in the database. returns the object with a filled id
     * @param subject. should not be null, should have a name that is not empty, and a ects value that is not emtpy and bigger than 0.
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    Subject create(Subject subject);

    /**
     * adds a lecturer to be responsible for a subject. if the lecturer already was responsible, the lectuerer object that was responsible in the past will be returned. otherwise the lecturer that was now added will be returned.
     * may throw a LecturerNotFoundException or a SubjectNotFoundException
     * 
     * @param subjectId. should not be null and not <1.
     * @param lecturerUisUserId
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    Lecturer addLecturerToSubject(Long subjectId, Long lecturerUisUserId);

    /**
     * returns a list of lectuerers that are responsible for a subject and match the search string. search startegy should be string.contains.
     * 
     * may throw SubjectNotFoundException if no subject was found
     * 
     * @param subjectId should not be null and not <1.
     * @param search. will be changed to "" if is null.
     * @return a maximum of 10 lecturers
     */
    @PreAuthorize("hasRole('ADMIN')")
    List<Lecturer> getAvailableLecturersForSubject(Long subjectId, String search);

    /**
     * Removes a lecturer from a subject
     *
     * only works if the user is ADMIN
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
