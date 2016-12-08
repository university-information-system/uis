package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import org.springframework.security.access.prepost.*;

import java.util.*;

public interface SemesterService {

    @PreAuthorize("hasRole('ADMIN')")
    Semester create(Semester semester);

    @PreAuthorize("isAuthenticated()")
    Semester getCurrentSemester();

    @PreAuthorize("isAuthenticated()")
    List<Semester> findAll();

    @PreAuthorize("isAuthenticated()")
    List<Semester> findAllSince(Semester semester);
}
