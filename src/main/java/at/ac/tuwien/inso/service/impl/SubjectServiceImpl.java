package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Subject> findAll(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Subject findOne(long id) {
        return subjectRepository.findSubjectById(id);
    }

    @Override
    @Transactional
    public Subject create(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> searchForSubjects(String word) {
        return subjectRepository.findByNameContainingIgnoreCase(word);
    }
}
