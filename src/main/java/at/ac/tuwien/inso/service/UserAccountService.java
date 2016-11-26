package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import org.springframework.security.core.userdetails.*;

public interface UserAccountService extends UserDetailsService {

    UserAccount getCurrentLoggedInUser();

    boolean existsUsername(String username);
}
