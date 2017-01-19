package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SubjectForStudyPlanRepository extends CrudRepository<SubjectForStudyPlan, Long> {

    List<SubjectForStudyPlan> findByStudyPlanIdOrderBySemesterRecommendation(Long id);

    List<SubjectForStudyPlan> findBySubject(Subject subject);

    SubjectForStudyPlan findBySubjectAndStudyPlan(Subject subject, StudyPlan studyPlan);
}
