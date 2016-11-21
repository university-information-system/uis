package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface StudentService {

    List<StudyPlanRegistration> findStudyPlanRegistrationsFor(Student student);

    Student findOne(Long id);

    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan);

    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan, Semester currentSemester);
}
