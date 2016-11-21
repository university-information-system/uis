package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface SubjectService {

    List<Subject> findAll();

    Subject findOne(long id);

    Subject create(Subject subject);

    List<Subject> searchForSubjects(String word);
}
