package at.ac.tuwien.inso.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.entity.UserAccount;

public interface StudentService {

	/**
	 * returns one student by id if he exists 
	 * @param id
	 * @return
	 */
    @PreAuthorize("isAuthenticated()")
    Student findOne(Long id);

    /**
     * returns one student by account
     * @param account
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    Student findOne(UserAccount account);

    @PreAuthorize("isAuthenticated()")
    Student findByUsername(String username);

    @PreAuthorize("hasRole('ADMIN')")
    List<StudyPlanRegistration> findStudyPlanRegistrationsFor(Student student);

    @PreAuthorize("hasRole('ADMIN')")
    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan);

    @PreAuthorize("hasRole('ADMIN')")
    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan, SemesterDto currentSemester);
}
