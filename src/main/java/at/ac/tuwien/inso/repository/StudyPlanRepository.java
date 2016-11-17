package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.repository.*;

public interface StudyPlanRepository extends CrudRepository<StudyPlan, Long> {
    StudyPlan findStudyPlanById(Long id);
}
