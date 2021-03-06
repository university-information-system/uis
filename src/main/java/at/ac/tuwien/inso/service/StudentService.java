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
	 * id should not be null and not <1
	 * 
	 * @param id
	 * @return
	 */
    @PreAuthorize("isAuthenticated()")
    Student findOne(Long id);

    /**
     * returns one student by account
     * user needs to be authenticated
     * @param account
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    Student findOne(UserAccount account);

    /**
     * returns a student by its username
     * user needs to be authenticated
     * @param username
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    Student findByUsername(String username);

    /**
     * returns all studyplanregistrations for the student. student should not be null!
     * user needs to be admin
     * @param student
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    List<StudyPlanRegistration> findStudyPlanRegistrationsFor(Student student);

    /**
     * registers a student to a studyplan for the current semester. student and studyplan should not be null
     * may start a new semester
     * 
     * user needs to be admin
     * 
     * @param student
     * @param studyPlan
     */
    @PreAuthorize("hasRole('ADMIN')")
    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan);

    /**
     * registers a student to a studyplan for the given semester
     * student and studyplan should not be null
     * 
     * user needs to be admin!
     * 
     * @param student
     * @param studyPlan
     * @param currentSemester
     */
    @PreAuthorize("hasRole('ADMIN')")
    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan, SemesterDto currentSemester);
}
