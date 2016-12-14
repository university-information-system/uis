package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

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

    @Query("select case when count(c) > 0 then true else false end " +
            "from Course c " +
            "where c = ?2 and ?1 member of c.students"
    )
    boolean existsCourseRegistration(Student student, Course course);
}