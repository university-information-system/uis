package at.ac.tuwien.inso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Student;

public interface GradeRepository extends CrudRepository<Grade, Long> {

    List<Grade> findByStudentAccountId(Long id);

    @Query("select g " +
            "from Grade g " +
            "where g.student = ?1")
    List<Grade> findAllOfStudent(Student student);
    
    List<Grade> findByCourseId(Long courseId);

    List<Grade> findByLecturerIdAndCourseId(Long lecturerId, Long courseId);
}
