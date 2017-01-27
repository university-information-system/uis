package at.ac.tuwien.inso.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.repository.TagRepository;
import at.ac.tuwien.inso.service.TagService;

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
