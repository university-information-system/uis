package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.dto.SemesterDto;
import org.springframework.security.access.prepost.*;

import java.util.*;

public interface SemesterService {

    @PreAuthorize("hasRole('ADMIN')")
    SemesterDto create(SemesterDto semester);

    @PreAuthorize("isAuthenticated()")
    SemesterDto getCurrentSemester();

    @PreAuthorize("isAuthenticated()")
    List<SemesterDto> findAll();

    @PreAuthorize("isAuthenticated()")
    List<SemesterDto> findAllSince(SemesterDto semester);
}
