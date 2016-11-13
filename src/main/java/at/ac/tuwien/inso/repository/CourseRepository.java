package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

    List<Course> findAllBySemester(Semester semester);

    List<Course> findAllBySemesterAndSubjectNameLikeIgnoreCase(Semester semester, String name);

    List<Course> findAllBySemesterAndSubject(Semester semester, Subject subject);

}