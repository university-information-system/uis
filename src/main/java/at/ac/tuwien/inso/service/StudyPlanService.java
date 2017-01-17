package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.*;

public interface StudyPlanService {

	/**
	 * creates a new study plan
	 * may throw a ValidationException if study plans name, or optional, mandatory or freechoice ects values are null or empty or <=0
	 *
	 * @param studyPlan
	 * @return
	 */
    @PreAuthorize("hasRole('ADMIN')")
    StudyPlan create(StudyPlan studyPlan);

    /**
     * returns a list of all StudyPlans.
     * user must be authorized
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    List<StudyPlan> findAll();

    /**
     * returns the StudyPlan with the corresponding id
     * may throw a BusinessObjectNotFoundException if there is no StudyPlan with this id
     * @param id should not be null and not <1
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    StudyPlan findOne(Long id);

    /**
     * try to find all SubjectsForStudyPlan by a study plan id.
     * should be ordered by semester recommendation
     * user must be authorized
     * 
     * @param id. should not be null and not <1
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    List<SubjectForStudyPlan> getSubjectsForStudyPlan(Long id);

    /**
     * 
     * @param id
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    List<SubjectWithGrade> getSubjectsWithGradesForStudyPlan(Long id);

    @PreAuthorize("hasRole('ADMIN')")
    void addSubjectToStudyPlan(SubjectForStudyPlan subjectForStudyPlan);

    /**
     * returns all available subjects for the study plan with the id. the subjects can be filtered with the query string
     * the search strategy of the query should be byNameContainingIgnoreCase(query)
     * 
     * user has to be authenticated
     * 
     * @param id. should not be null and not <1
     * @param query
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    List<Subject> getAvailableSubjectsForStudyPlan(Long id, String query);

    /**
     * disables the study plan of the given id.
     * 
     * user needs role ADMIN
     * may throw BusinessObjectNotFoundException if the study plan with this id does not exists
     * may throw a ValidationException if the id is not correct
     * 
     * @param id. should not be null and not <1
     */
    @PreAuthorize("hasRole('ADMIN')")
    StudyPlan disableStudyPlan(Long id);

	/**
	 * removes a given subject s from the study plan sp
	 * user need role ADMIN
	 * 
	 * @author m.pazourek
	 * @param sp should not be null and the sp.id should not be <1 and not null
	 * @param s should have an id
	 */
	@PreAuthorize("hasRole('ADMIN')")
    void removeSubjectFromStudyPlan(StudyPlan sp, Subject s);
}
