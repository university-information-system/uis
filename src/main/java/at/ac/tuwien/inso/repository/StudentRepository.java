package at.ac.tuwien.inso.repository;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.utils.TagFrequency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Long> {

    @Query("select new at.ac.tuwien.inso.repository.utils.TagFrequency(t, count(t)) " +
            "from Student s join s.courses c join c.tags t " +
            "where s = ?1 " +
            "group by t")
    List<TagFrequency> computeTagsFrequencyFor(Student student);

    Student findByAccount(UserAccount account);
}
