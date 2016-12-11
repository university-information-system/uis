package at.ac.tuwien.inso.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import at.ac.tuwien.inso.entity.Lecturer;

public interface LecturerRepository extends CrudRepository<Lecturer, Long> {

    @Query("select l from Lecturer l where ACCOUNT_ID = ?1")
    Lecturer findLecturerByAccountId(Long id);

    public List<Lecturer> findAll();

    public Lecturer findById(Long id);
}
