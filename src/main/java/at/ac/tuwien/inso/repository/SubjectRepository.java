package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {

    List<Subject> findByLecturers_Id(Long id);

    Subject findSubjectById(Long id);

    List<Subject> findByNameContainingIgnoreCase(String name);
}
