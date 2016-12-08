package at.ac.tuwien.inso.dao_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

import static at.ac.tuwien.inso.utils.IterableUtils.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SemesterRepositoryTests {

    @Autowired
    private SemesterRepository semesterRepository;

    private List<Semester> semesters;

    @Before
    public void setUp() throws Exception {
        semesters = toList(semesterRepository.save(asList(
                new Semester("1"),
                new Semester("2")
        )));
    }

    @Test
    public void findAllSinceWithCurrentSemester() throws Exception {
        assertThat(semesterRepository.findAllSince(semesters.get(1)), equalTo(singletonList(semesters.get(1))));
    }

    @Test
    public void findAllSinceWithPastSemester() throws Exception {
        assertThat(semesterRepository.findAllSince(semesters.get(0)), equalTo(asList(semesters.get(1), semesters.get(0))));
    }
}
