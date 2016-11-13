package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.*;
import org.springframework.data.repository.*;

public interface PendingAccountActivationRepository extends CrudRepository<PendingAccountActivation, String> {
}
