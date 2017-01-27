package at.ac.tuwien.inso.repository;

import org.springframework.data.repository.CrudRepository;

import at.ac.tuwien.inso.entity.PendingAccountActivation;

public interface PendingAccountActivationRepository extends CrudRepository<PendingAccountActivation, String> {
}
