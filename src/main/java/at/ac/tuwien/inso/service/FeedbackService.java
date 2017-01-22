package at.ac.tuwien.inso.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Student;

@Service
public interface FeedbackService {

    @PreAuthorize("isAuthenticated()")
    List<Feedback> findAllOfStudent(Student student);

    @PreAuthorize("isAuthenticated()")
    Feedback save(Feedback feedback);

    @PreAuthorize("isAuthenticated()")
    List<Feedback> findFeedbackForCourse(Long id);
}
