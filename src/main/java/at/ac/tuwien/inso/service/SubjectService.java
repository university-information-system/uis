package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.repository.SubjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Iterable<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findSubjectById(id);
    }
    
    public Subject create(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Iterable<Subject> searchForSubjects(String word) {
        return subjectRepository.findByNameContainingIgnoreCase(word);
    }
}
