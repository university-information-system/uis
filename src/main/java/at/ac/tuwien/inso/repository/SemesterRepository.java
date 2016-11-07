package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.Semester;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends CrudRepository<Semester, Long> {
}
