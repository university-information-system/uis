package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
public class UisUserService {

    @Autowired
    private UisUserRepository uisUserRepository;

    @Transactional(readOnly = true)
    public Iterable<UisUser> findAll() {
        return uisUserRepository.findAll();
    }
}
