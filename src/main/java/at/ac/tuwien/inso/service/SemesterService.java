package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.dto.SemesterDto;
import org.springframework.security.access.prepost.*;

import java.util.*;

public interface SemesterService {

    @PreAuthorize("hasRole('ADMIN')")
    SemesterDto create(SemesterDto semester);

    /**
     * Get the current semester, only use it in tests!
     *
     * Always use getOrCreateCurrentSemester:
     * it also checks if a new semester can be started and starts it automatically.
     */
    @PreAuthorize("isAuthenticated()")
    SemesterDto getCurrentSemester();

    /**
     * Get the current semester.
     *
     * If a new semester can be started, the new semester will be started and returned.
     */
    @PreAuthorize("isAuthenticated()")
    SemesterDto getOrCreateCurrentSemester();

    @PreAuthorize("isAuthenticated()")
    List<SemesterDto> findAll();

    @PreAuthorize("isAuthenticated()")
    List<SemesterDto> findAllSince(SemesterDto semester);
}
