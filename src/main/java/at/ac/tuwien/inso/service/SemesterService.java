package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface SemesterService {
    Semester create(Semester semester);

    Semester getCurrentSemester();

    List<Semester> findAll();
}
