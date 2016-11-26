package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface SubjectService {

    @PreAuthorize("isAuthenticated()")
    List<Subject> findAll();

    @PreAuthorize("isAuthenticated()")
    Subject findOne(long id);

    @PreAuthorize("isAuthenticated()")
    List<Subject> searchForSubjects(String word);

    @PreAuthorize("hasRole('ADMIN')")
    Subject create(Subject subject);
}
