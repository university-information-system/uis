package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long> {

    Course findById(Long Id);
    List<Course> findAll();
}