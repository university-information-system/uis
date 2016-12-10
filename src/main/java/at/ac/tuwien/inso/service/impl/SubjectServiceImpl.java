package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
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

    @Autowired
    private LecturerRepository lecturerRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Subject> findAll(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Subject findOne(long id) {
        return subjectRepository.findById(id);
    }

    @Override
    @Transactional
    public Subject create(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    @Transactional
    public Subject addLecturerToSubject(Long subjectId, Long lecturerUisUserId) {
        Lecturer lecturer = lecturerRepository.findById(lecturerUisUserId);

        if (lecturer == null) {
            String msg = "Lecturer with user id " + lecturerUisUserId + " not found";
            throw new BusinessObjectNotFoundException(msg);
        }

        Subject subject = subjectRepository.findById(subjectId);

        if (subject == null) {
            String msg = "Subject with id " + lecturerUisUserId + " not found";
            throw new BusinessObjectNotFoundException(msg);
        }

        subject.addLecturers(lecturer);

        Subject savedSubject = subjectRepository.save(subject);

        return savedSubject;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Lecturer> getAvailableLecturersForSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId);

        if (subject == null) {
            String msg = "Subject with id '" + subjectId + "' not found";
            throw new BusinessObjectNotFoundException(msg);
        }

        List<Lecturer> currentLecturers = subject.getLecturers();

        List<Lecturer> allLecturers = lecturerRepository.findAll();

        return allLecturers
                .stream()
                .filter(lecturer -> !currentLecturers.contains(lecturer))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> searchForSubjects(String word) {
        return subjectRepository.findByNameContainingIgnoreCase(word);
    }


}
