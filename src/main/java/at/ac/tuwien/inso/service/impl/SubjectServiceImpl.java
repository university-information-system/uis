package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private CourseService courseService;

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
            throw new SubjectNotFoundException(msg);
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
            throw new SubjectNotFoundException("Subject with id '" + subjectId + "' not found");
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

	@Override
	public boolean delete(Subject subject) throws ValidationException{
		List<Course> courses = courseService.findCoursesForSubject(subject);
		if(!courses.isEmpty()){
			throw new ValidationException("Cannot delete Subject because there are courses");
		}else{
			subjectRepository.delete(subject);
			return true;
		}
	}


}
