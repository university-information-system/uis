package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface LecturerService {
    Lecturer getLoggedInLecturer();

    Iterable<Subject> getOwnSubjects();

    List<Subject> findSubjectsFor(Lecturer lecturer);
}
