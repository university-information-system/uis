package at.ac.tuwien.inso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.ac.tuwien.inso.entity.Lecturer;

public interface LecturerRepository extends CrudRepository<Lecturer, Long> {

    @Query("select l from Lecturer l where ACCOUNT_ID = ?1")
    Lecturer findLecturerByAccountId(Long id);


    public List<Lecturer> findAllByIdentificationNumberLikeIgnoreCaseOrNameLikeIgnoreCase(
            String identificationNumber,
            String name
    );

    public Lecturer findById(Long id);
}
