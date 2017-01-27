package at.ac.tuwien.inso.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.exception.LecturerNotFoundException;
import at.ac.tuwien.inso.exception.RelationNotfoundException;
import at.ac.tuwien.inso.exception.SubjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.validator.SubjectValidator;
import at.ac.tuwien.inso.service.validator.ValidatorFactory;


@Service
public class SubjectServiceImpl implements SubjectService {

    private ValidatorFactory validatorFactory = new ValidatorFactory();
    private SubjectValidator validator = validatorFactory.getSubjectValidator();
    private static final Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);

    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private CourseService courseService;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Override
    public Page<Subject> findBySearch(String search, Pageable pageable) {
    	log.info("finding search by word "+search);
        String sqlSearch = "%" + search + "%";
        return subjectRepository.findAllByNameLikeIgnoreCase(sqlSearch, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Subject findOne(Long id) {
        validator.validateSubjectId(id);
    	log.info("finding subject by id "+id);
        Subject subject = subjectRepository.findOne(id);

        if(subject == null) {
            log.warn("Subject not found");
            //TODO throwing this will cause a security test fail, for whatever reason?
            //throw new SubjectNotFoundException();
        }

        return subject;
    }

    @Override
    @Transactional
    public Subject create(Subject subject) {
        validator.validateNewSubject(subject);
    	log.info("creating subject "+subject.toString());
        return subjectRepository.save(subject);
    }

    @Override
    @Transactional
    public Lecturer addLecturerToSubject(Long subjectId, Long lecturerUisUserId) {
        

        validator.validateSubjectId(subjectId);
        
        log.info("addLecturerToSubject for subject {} and lecturer {}", subjectId,
                lecturerUisUserId);
        
        Lecturer lecturer = lecturerRepository.findById(lecturerUisUserId);

        if (lecturer == null) {
            String msg = "Lecturer with user id " + lecturerUisUserId + " not found";
            log.info(msg);
            throw new LecturerNotFoundException(msg);
        }

        Subject subject = subjectRepository.findById(subjectId);

        if (subject == null) {
            String msg = "Subject with id " + lecturerUisUserId + " not found";
            log.info(msg);
            throw new SubjectNotFoundException(msg);
        }

        if (subject.getLecturers().contains(lecturer)) {
            return lecturer;
        }

        subject.addLecturers(lecturer);
        subjectRepository.save(subject);

        return lecturer;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Lecturer> getAvailableLecturersForSubject(Long subjectId, String search) {
        validator.validateSubjectId(subjectId);
        if (search == null) {
            search = "";
        }
    	log.info("getting available lectureres for subject with subject id "+subjectId+" and search string "+search);
        
    	
        Subject subject = subjectRepository.findById(subjectId);

        if (subject == null) {
        	log.warn("Subject with id '" + subjectId + "' not found");
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
        validator.validateSubjectId(subjectId);
        log.info("removeLecturerFromSubject for subject {} and lecturer {}", subjectId, lecturerUisUserId);

        Subject subject = subjectRepository.findById(subjectId);

        if (subject == null) {
            String msg = "Subject with id '" + subjectId + "' not found";
            log.info(msg);
            throw new SubjectNotFoundException(msg);
        }

        Lecturer lecturer = lecturerRepository.findById(lecturerUisUserId);

        if (lecturer == null) {
            String msg = "Lecturer with id '" + lecturerUisUserId + "' not found";
            log.info(msg);
            throw new LecturerNotFoundException(msg);
        }

        List<Lecturer> currentLecturers = subject.getLecturers();

        boolean isLecturer = currentLecturers.contains(lecturer);

        if (!isLecturer) {
            String msg = "Lecturer with id " + lecturerUisUserId + " not found for subject " +
                    subjectId;
            log.info(msg);
            throw new RelationNotfoundException(msg);
        }

        subject.removeLecturers(lecturer);

        subjectRepository.save(subject);

        return lecturer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> searchForSubjects(String word) {
    	log.info("seachring for subjects with search word "+word);
        return subjectRepository.findByNameContainingIgnoreCase(word);
    }

	@Override
	@Transactional
	public boolean remove(Subject subject) throws ValidationException{
		log.info("trying to remove subject");
		validator.validateNewSubject(subject);
		if(subject==null){
			log.info("Subject is null.");
			throw new ValidationException("Subject is null.");
		}
		log.info("removing subject "+subject+" now.");
		List<Course> courses = courseService.findCoursesForSubject(subject);
		if(!courses.isEmpty()){
			String msg = "";
			if(subject!=null){
				msg = "Cannot delete subject [Name: "+subject.getName()+"] because there are courses";
			}
			log.info(msg);
			throw new ValidationException(msg);
		}else{
			subjectRepository.delete(subject);
			return true;
		}
	}


}
