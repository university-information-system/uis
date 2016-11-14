package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.repository.SubjectRepository;

import java.math.BigDecimal;

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
    
    public Subject getNewSubject(){
    	return new Subject("",null);
    }
}
