package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface GradeRepository extends CrudRepository<Grade, Long> {

    List<Grade> findByStudentAccountId(Long id);

    @Query("select g " +
            "from Grade g " +
            "where g.student = ?1")
    List<Grade> findAllOfStudent(Student student);
    
    List<Grade> findByCourseId(Long courseId);

    List<Grade> findByLecturerIdAndCourseId(Long lecturerId, Long courseId);
}
