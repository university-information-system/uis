package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    @Transactional
    public Semester create(Semester semester) {
        return semesterRepository.save(semester);
    }

    @Override
    @Transactional(readOnly = true)
    public Semester getCurrentSemester() {
        return semesterRepository.findFirstByOrderByIdDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Semester> findAll() {
        return semesterRepository.findAllByOrderByIdDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Semester> findAllSince(Semester semester) {
        return semesterRepository.findAllSince(semester);
    }
}
