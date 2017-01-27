package at.ac.tuwien.inso.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.inso.entity.Subject;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {

    List<Subject> findByLecturers_Id(Long id);

    Subject findById(Long id);

    List<Subject> findByNameContainingIgnoreCase(String name);

    Page<Subject> findAll(Pageable pageable);

    Page<Subject> findAllByNameLikeIgnoreCase(String name, Pageable pageable);
}
