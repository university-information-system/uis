package at.ac.tuwien.inso.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.LecturerNotFoundException;
import at.ac.tuwien.inso.exception.RelationNotfoundException;
import at.ac.tuwien.inso.exception.SubjectNotFoundException;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.service.SubjectService;


@Service
public class SubjectServiceImpl implements SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

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
    public Lecturer addLecturerToSubject(Long subjectId, Long lecturerUisUserId) {
        logger.info("addLecturerToSubject for subject {} and lecturer {}", subjectId,
                lecturerUisUserId);

        Lecturer lecturer = lecturerRepository.findById(lecturerUisUserId);

        if (lecturer == null) {
            String msg = "Lecturer with user id " + lecturerUisUserId + " not found";
            logger.info(msg);
            throw new LecturerNotFoundException(msg);
        }

        Subject subject = subjectRepository.findById(subjectId);

        if (subject == null) {
            String msg = "Subject with id " + lecturerUisUserId + " not found";
            logger.info(msg);
            throw new SubjectNotFoundException(msg);
        }

        if (subject.getLecturers().contains(lecturer)) {
            return lecturer;
        }

        subject.addLecturers(lecturer);
        Subject savedSubject = subjectRepository.save(subject);

        return lecturer;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Lecturer> getAvailableLecturersForSubject(Long subjectId, String search) {
        if (search == null) {
            search = "";
        }

        Subject subject = subjectRepository.findById(subjectId);

        if (subject == null) {
            throw new SubjectNotFoundException("Subject with id '" + subjectId + "' not found");
        }

        List<Lecturer> currentLecturers = subject.getLecturers();

        List<Lecturer> searchedLecturers =
                lecturerRepository.findAllByIdentificationNumberLikeIgnoreCaseOrNameLikeIgnoreCase(
                        "%" + search + "%",
                        "%" + search + "%"
                );

        return searchedLecturers
                .stream()
                .filter(lecturer -> !currentLecturers.contains(lecturer))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public Lecturer removeLecturerFromSubject(Long subjectId, Long lecturerUisUserId) {
        logger.info("removeLecturerFromSubject for subject {} and lecturer {}", subjectId,
                lecturerUisUserId);

        Subject subject = subjectRepository.findById(subjectId);

        if (subject == null) {
            String msg = "Subject with id '" + subjectId + "' not found";
            logger.info(msg);
            throw new SubjectNotFoundException(msg);
        }

        Lecturer lecturer = lecturerRepository.findById(lecturerUisUserId);

        if (lecturer == null) {
            String msg = "Lecturer with id '" + lecturerUisUserId + "' not found";
            logger.info(msg);
            throw new LecturerNotFoundException(msg);
        }

        List<Lecturer> currentLecturers = subject.getLecturers();

        boolean isLecturer = currentLecturers.contains(lecturer);

        if (!isLecturer) {
            String msg = "Lecturer with id " + lecturerUisUserId + " not found for subject " +
                    subjectId;
            logger.info(msg);
            throw new RelationNotfoundException(msg);
        }

        subject.removeLecturers(lecturer);

        subjectRepository.save(subject);

        return lecturer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> searchForSubjects(String word) {
        return subjectRepository.findByNameContainingIgnoreCase(word);
    }
}
