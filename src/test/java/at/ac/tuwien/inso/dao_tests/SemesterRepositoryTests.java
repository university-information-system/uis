package at.ac.tuwien.inso.dao_tests;

import static at.ac.tuwien.inso.utils.IterableUtils.toList;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.repository.SemesterRepository;

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
                new Semester(2016, SemesterType.WinterSemester),
                new Semester(2016, SemesterType.SummerSemester)
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
