package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface UisUserRepository extends JpaRepository<UisUser, Long> {

    @Query("select u " +
            "from UisUser u " +
            "where u.identificationNumber = ?1 " +
            "or lower(u.name) like concat('%', lower(?1), '%') " +
            "or lower(u.email) like concat('%', lower(?1), '%') " +
            "order by u.id desc"
    )
    Page<UisUser> findAllMatching(String searchFilter, Pageable pageable);

    @Query("select case when count(user) > 0 then true else false end " +
            "from UisUser user " +
            "where user.identificationNumber = ?1"
    )
    boolean existsByIdentificationNumber(String identificationNumber);
}
