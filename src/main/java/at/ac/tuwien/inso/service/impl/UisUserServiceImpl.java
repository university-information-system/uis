package at.ac.tuwien.inso.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.repository.UisUserRepository;
import at.ac.tuwien.inso.service.UisUserService;
import at.ac.tuwien.inso.validator.UisUserValidator;
import at.ac.tuwien.inso.validator.ValidatorFactory;

@Service
public class UisUserServiceImpl implements UisUserService {

    private ValidatorFactory validatorFactory = new ValidatorFactory();
    private UisUserValidator validator = validatorFactory.getUisUserValidator();

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
    public UisUser findOne(long id) throws BusinessObjectNotFoundException {
    	log.info("finding UisUser for id "+id);
        validator.validateUisUserId(id);
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
