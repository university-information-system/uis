package at.ac.tuwien.inso.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.UserAccountRepository;
import at.ac.tuwien.inso.service.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    // UserAccountService extends org.springframework.security.core.userdetails.UserDetailsService


	private static final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);
	
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loading user by username with username "+username);
    	UserAccount user = userAccountRepository.findByUsername(username);

        if (user == null){
        	log.info("cannot find user with username "+username);
        	throw new UsernameNotFoundException(username);
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount getCurrentLoggedInUser() {
    	log.info("getting currently logged in user");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("returning logged in user with name "+ auth.getName());
        return loadUserByUsername(auth.getName());
    }

    @Override
    public boolean existsUsername(String username) {
    	log.info("checking if username "+username+" exists");
        return userAccountRepository.existsByUsername(username);
    }
}
