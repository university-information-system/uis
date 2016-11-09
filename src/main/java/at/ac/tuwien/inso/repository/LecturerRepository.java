package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;

public interface LecturerRepository extends CrudRepository<Lecturer, Long> {

    @Query("select l from Lecturer l where ACCOUNT_ID = ?1")
    Lecturer findLecturerByAccountId(Long id);
}
