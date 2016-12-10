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
}
