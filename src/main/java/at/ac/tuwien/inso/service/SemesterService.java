package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.repository.SemesterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    public Semester getCurrentSemester() {
        return semesterRepository.findFirstByOrderByIdDesc();
    }

}
