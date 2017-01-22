package at.ac.tuwien.inso.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;

@Repository
public interface SubjectForStudyPlanRepository extends CrudRepository<SubjectForStudyPlan, Long> {

    List<SubjectForStudyPlan> findByStudyPlanIdOrderBySemesterRecommendation(Long id);

    List<SubjectForStudyPlan> findBySubject(Subject subject);

    List<SubjectForStudyPlan> findBySubjectInAndStudyPlan(List<Subject> subjects, StudyPlan studyPlan);
}
