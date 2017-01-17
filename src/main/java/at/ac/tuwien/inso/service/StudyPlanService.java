package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.*;

public interface StudyPlanService {

	/**
	 * creates a new study plan
	 * may throw a ValidationException if studyplans name, or optional, mandatory or freechoice ects values are null or empty or <=0
	 *
	 * @param studyPlan
	 * @return
	 */
    @PreAuthorize("hasRole('ADMIN')")
    StudyPlan create(StudyPlan studyPlan);

    /**
     * returns a list of all StudyPlans.
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    List<StudyPlan> findAll();

    @PreAuthorize("isAuthenticated()")
    StudyPlan findOne(Long id);

    @PreAuthorize("isAuthenticated()")
    List<SubjectForStudyPlan> getSubjectsForStudyPlan(Long id);

    @PreAuthorize("isAuthenticated()")
    List<SubjectWithGrade> getSubjectsWithGradesForStudyPlan(Long id);

    @PreAuthorize("hasRole('ADMIN')")
    void addSubjectToStudyPlan(SubjectForStudyPlan subjectForStudyPlan);

    @PreAuthorize("isAuthenticated()")
    List<Subject> getAvailableSubjectsForStudyPlan(Long id, String query);

    /**
     * disables the studyplan of the given id.
     * @author m.pazourek
     * @param id
     */
    @PreAuthorize("hasRole('ADMIN')")
    StudyPlan disableStudyPlan(Long id);

	/**
	 * removes a given subject s from the studyplan sp
	 * @author m.pazourek
	 * @param sp
	 * @param s
	 */
	@PreAuthorize("hasRole('ADMIN')")
    void removeSubjectFromStudyPlan(StudyPlan sp, Subject s);
}
