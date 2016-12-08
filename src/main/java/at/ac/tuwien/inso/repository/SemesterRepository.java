package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SemesterRepository extends CrudRepository<Semester, Long> {

    Semester findFirstByOrderByIdDesc();

    List<Semester> findAllByOrderByIdDesc();

    @Query("select s " +
            "from Semester s " +
            "where s.id >= :#{#semester.id} " +
            "order by s.id desc")
    List<Semester> findAllSince(@Param("semester") Semester semester);
}
