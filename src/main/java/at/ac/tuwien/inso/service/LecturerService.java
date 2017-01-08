package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

public interface LecturerService {

    @PreAuthorize("hasRole('LECTURER')")
    Lecturer getLoggedInLecturer();

    @PreAuthorize("hasRole('LECTURER')")
    Iterable<Subject> getOwnSubjects();

    @PreAuthorize("isAuthenticated()")
    List<Subject> findSubjectsFor(Lecturer lecturer);

    @PreAuthorize("hasRole('ADMIN')")
    String generateQRUrl(Lecturer lecturer) throws UnsupportedEncodingException;
}
