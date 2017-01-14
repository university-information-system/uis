package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

	private static final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

	
    @Autowired
    private TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAll() {
    	log.info("finding all tags");
        return tagRepository.findAll();
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }

}
