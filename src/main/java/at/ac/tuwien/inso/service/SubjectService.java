package at.ac.tuwien.inso.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface SubjectService {

    @PreAuthorize("isAuthenticated()")
    Page<Subject> findAll(Pageable pageable);

    @PreAuthorize("isAuthenticated()")
    Subject findOne(long id);

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
     * @param subjectId
     * @param lecturerUisUserId
     * @return the removed lecturer
     */
    @PreAuthorize("hasRole('ADMIN')")
    Lecturer removeLecturerFromSubject(Long subjectId, Long lecturerUisUserId);
}
