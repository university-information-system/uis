package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
public class UserAccountService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountRepository.findByUsername(username);

        if (user == null) throw new UsernameNotFoundException(username);

        return user;
    }

    public UserAccount getCurrentLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return loadUserByUsername(auth.getName());
    }
}
