package at.ac.tuwien.inso.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.dto.SemesterDto;

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

    /**
     * trys to return all semesters as SemesterDto
     * 
     * user needs to be authenticated
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    List<SemesterDto> findAll();

    @PreAuthorize("isAuthenticated()")
    List<SemesterDto> findAllSince(SemesterDto semester);
}
