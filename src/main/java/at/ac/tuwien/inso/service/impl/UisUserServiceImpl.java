package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class UisUserServiceImpl implements UisUserService {

    @Autowired
    private UisUserRepository uisUserRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UisUser> findAllMatching(String searchFilter) {
        return uisUserRepository.findAllMatching(searchFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public UisUser findOne(long id) {
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
