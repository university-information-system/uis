package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SemesterRepository extends CrudRepository<Semester, Long> {

    Semester findFirstByOrderByIdDesc();

    List<Semester> findAllByOrderByIdDesc();

}
