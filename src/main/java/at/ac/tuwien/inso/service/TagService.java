package at.ac.tuwien.inso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.repository.TagRepository;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

}
