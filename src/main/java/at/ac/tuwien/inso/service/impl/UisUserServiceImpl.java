package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import javax.persistence.*;
import java.util.*;

@Service
public class UisUserServiceImpl implements UisUserService {

    @Autowired
    private UisUserRepository uisUserRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UisUser> findAll() {
        return uisUserRepository.findAllByOrderByIdDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public UisUser findOne(long id) {
        UisUser user = uisUserRepository.findOne(id);
        if (user == null) {
            throw new EntityNotFoundException("Invalid user id: " + id);
        }

        return user;
    }
}
