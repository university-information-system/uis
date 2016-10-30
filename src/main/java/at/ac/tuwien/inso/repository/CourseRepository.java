package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

    List<Course> findAll();
}