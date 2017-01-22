package at.ac.tuwien.inso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.utils.TagFrequency;

public interface StudentRepository extends CrudRepository<Student, Long> {

    @Query("select new at.ac.tuwien.inso.repository.utils.TagFrequency(t, count(t)) " +
            "from Course c join c.tags t " +
            "where ?1 member of c.students " +
            "group by t")
    List<TagFrequency> computeTagsFrequencyFor(Student student);

    Student findByAccount(UserAccount account);

    @Query("select s " +
            "from Student s join s.account a " +
            "where a.username = ?1")
    Student findByUsername(String username);
}
