package at.ac.tuwien.inso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.inso.entity.Semester;

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
