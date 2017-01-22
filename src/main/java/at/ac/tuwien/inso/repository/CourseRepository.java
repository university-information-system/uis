package at.ac.tuwien.inso.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

    Page<Course> findAllBySemesterAndSubjectNameLikeIgnoreCase(Semester semester, String name, Pageable pageable);

    List<Course> findAllBySemesterAndSubject(Semester semester, Subject subject);
    
    List<Course> findAllBySubject(Subject subject);

    @Query("select c " +
            "from Course c " +
            "where c.semester = (" +
            "   select s " +
            "   from Semester s " +
            "   where s.id = ( " +
            "       select max(s1.id) " +
            "       from Semester s1 " +
            "       )" +
            "   ) " +
            "and :student not member of c.students " +
            "and c.subject not in (" +
            "   select g.course.subject " +
            "   from Grade g " +
            "   where g.student = :student and g.mark.mark <> 5" +
            ")")
    List<Course> findAllRecommendableForStudent(@Param("student") Student student);

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