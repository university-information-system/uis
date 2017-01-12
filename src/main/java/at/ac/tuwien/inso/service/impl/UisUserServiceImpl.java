package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.validator.UisUserValidator;
import at.ac.tuwien.inso.service.validator.ValidatorFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
public class UisUserServiceImpl implements UisUserService {

    private ValidatorFactory validatorFactory = new ValidatorFactory();
    private UisUserValidator validator = validatorFactory.getUisUserValidator();

    @Autowired
    private UisUserRepository uisUserRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UisUser> findAllMatching(String searchFilter, Pageable pageable) {
        return uisUserRepository.findAllMatching(searchFilter, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public UisUser findOne(long id) {
        validator.validateUisUserId(id);
        UisUser user = uisUserRepository.findOne(id);
        if (user == null) {
            throw new BusinessObjectNotFoundException("Invalid user id: " + id);
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUserWithIdentificationNumber(String id) {
        return uisUserRepository.existsByIdentificationNumber(id);
    }
}
