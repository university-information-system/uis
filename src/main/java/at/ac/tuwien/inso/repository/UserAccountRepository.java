package at.ac.tuwien.inso.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.inso.entity.UserAccount;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {

    UserAccount findByUsername(String username);

    @Query("select case when count(u) > 0 then true else false end " +
            "from UserAccount u " +
            "where u.username = ?1")
    boolean existsByUsername(String username);
}
