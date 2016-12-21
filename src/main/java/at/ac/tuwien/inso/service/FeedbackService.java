package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public interface FeedbackService {

    @PreAuthorize("isAuthenticated()")
    List<Feedback> findAllOfStudent(Student student);

    @PreAuthorize("isAuthenticated()")
    Feedback save(Feedback feedback);

    @PreAuthorize("isAuthenticated()")
    List<Feedback> findFeedbackForCourse(Long id);
}
