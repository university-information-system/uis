package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface SemesterService {

    @PreAuthorize("hasRole('ADMIN')")
    Semester create(Semester semester);

    @PreAuthorize("isAuthenticated()")
    Semester getCurrentSemester();

    @PreAuthorize("isAuthenticated()")
    List<Semester> findAll();
}
