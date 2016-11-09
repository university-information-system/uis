package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.repository.UisUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UisUserService {

    @Autowired
    private UisUserRepository uisUserRepository;

    @Transactional(readOnly = true)
    public List<UisUser> findAll() {
        return uisUserRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public UisUser findOne(long id) {
        UisUser user = uisUserRepository.findOne(id);
        if (user == null) {
            throw new EntityNotFoundException("Invalid user id: " + id);
        }

        return user;
    }
}
