package at.ac.tuwien.inso.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import at.ac.tuwien.inso.entity.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    public List<Tag> findAll();

    Tag findByName(String name);
}
