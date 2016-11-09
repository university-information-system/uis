package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UisUserRepository extends CrudRepository<UisUser, Long> {

    List<UisUser> findAllByOrderByIdDesc();
}
