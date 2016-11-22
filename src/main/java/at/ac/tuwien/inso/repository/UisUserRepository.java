package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UisUserRepository extends CrudRepository<UisUser, Long> {

    List<UisUser> findAllByOrderByIdDesc();

    @Query("select case when count(user) > 0 then true else false end " +
            "from UisUser user " +
            "where user.identificationNumber = ?1")
    boolean existsByIdentificationNumber(String identificationNumber);
}
