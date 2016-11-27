package at.ac.tuwien.inso.repository;

import org.springframework.data.repository.CrudRepository;

import at.ac.tuwien.inso.entity.Grade;

public interface GradeRepository extends CrudRepository<Grade, Long> {
}
