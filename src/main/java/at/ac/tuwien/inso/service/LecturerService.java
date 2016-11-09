package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LecturerService {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public Lecturer getLoggedInLecturer() {
        Long id = userAccountService.getCurrentLoggedInUser().getId();
        return lecturerRepository.findLecturerByAccountId(id);
    }

    public Iterable<Subject> getOwnSubjects() {
        return subjectRepository.findByLecturers_Id(getLoggedInLecturer().getId());
    }

    @Transactional(readOnly = true)
    public List<Subject> findSubjectsFor(Lecturer lecturer) {
        return lecturer.getSubjects();
    }
}
