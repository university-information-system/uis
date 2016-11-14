package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.repository.SemesterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    public Semester create(Semester semester) {
        return semesterRepository.save(semester);
    }

    public Semester getCurrentSemester() {
        return semesterRepository.findFirstByOrderByIdDesc();
    }

    public Iterable<Semester> getAllSemesters() {
        return semesterRepository.findAllByOrderByIdDesc();
    }
}
