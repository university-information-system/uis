package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectForStudyPlanRepository extends CrudRepository<SubjectForStudyPlan, Long> {
}
