package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {

    @Query("select f " +
            "from Feedback f " +
            "where f.student = ?1")
    List<Feedback> findAllOfStudent(Student student);
}
