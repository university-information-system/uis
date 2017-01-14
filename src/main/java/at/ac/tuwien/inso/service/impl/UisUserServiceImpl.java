package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
public class UisUserServiceImpl implements UisUserService {

	private static final Logger log = LoggerFactory.getLogger(UisUserServiceImpl.class);
	
    @Autowired
    private UisUserRepository uisUserRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UisUser> findAllMatching(String searchFilter, Pageable pageable) {
    	log.info("find all matching UisUsers for searchFilter "+searchFilter+" and Pageable "+pageable);
        return uisUserRepository.findAllMatching(searchFilter, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public UisUser findOne(long id) {
    	log.info("finding UisUser for id "+id);
        UisUser user = uisUserRepository.findOne(id);
        if (user == null) {
        	log.warn("can not find UisUser: Invalid user id: " + id );
        	throw new BusinessObjectNotFoundException("Invalid user id: " + id);
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUserWithIdentificationNumber(String id) {
    	log.info("trying to find boolean if user with id "+id+" exists.");
        return uisUserRepository.existsByIdentificationNumber(id);
    }
}
