package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {

    Iterable<Subject> findByLecturers_Id(Long id);

    Subject findSubjectById(Long id);

    Iterable<Subject> findByNameContainingIgnoreCase(String name);
}
