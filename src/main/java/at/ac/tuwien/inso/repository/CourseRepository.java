package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

    List<Course> findAllBySemesterAndSubjectNameLikeIgnoreCase(Semester semester, String name);

    List<Course> findAllBySemesterAndSubject(Semester semester, Subject subject);

    @Query("select c " +
            "from Course c " +
            "where c.semester = (" +
            "   select s " +
            "   from Semester s " +
            "   where s.id = ( " +
            "       select max(s1.id) " +
            "       from Semester s1 " +
            "       )" +
            "   )" +
            "")
    List<Course> findAllByCurrentSemester();

    @Query("select c " +
            "from Course c " +
            "where ?1 member of c.students")
    List<Course> findAllForStudent(Student student);
}