package at.ac.tuwien.inso.service.course_recommendation.user_based;

import at.ac.tuwien.inso.service.student_subject_prefs.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.hamcrest.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.util.*;

import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.apache.commons.lang.ArrayUtils.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentNeighborhoodStoreImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private StudentNeighborhoodStore neighborhoodStore = new StudentNeighborhoodStoreImpl();

    @Test
    public void itBuildsStudentNeighborhood() throws Exception {
        givenStudentPreferences(
                new StudentSubjectPreference(1L, 10L, 5.0),
                new StudentSubjectPreference(1L, 11L, 1.0),
                new StudentSubjectPreference(1L, 12L, 3.0),
                new StudentSubjectPreference(2L, 10L, 1.0),
                new StudentSubjectPreference(2L, 11L, 3.0),
                new StudentSubjectPreference(2L, 12L, 1.0),
                new StudentSubjectPreference(3L, 10L, 5.0),
                new StudentSubjectPreference(3L, 11L, 3.0),
                new StudentSubjectPreference(3L, 12L, 3.0)
        );

        thenCheckSimilarStudents(1L, singletonList(3L));
    }

    private void givenStudentPreferences(StudentSubjectPreference... studentSubjectPreferences) {
        Mockito.when(mongoTemplate.stream(new Query(), StudentSubjectPreference.class))
                .thenReturn(new CloseableIterator<StudentSubjectPreference>() {

                    private int current = 0;

                    @Override
                    public void close() {

                    }

                    @Override
                    public boolean hasNext() {
                        return current < studentSubjectPreferences.length;
                    }

                    @Override
                    public StudentSubjectPreference next() {
                        return studentSubjectPreferences[current++];
                    }
                });
    }

    private void thenCheckSimilarStudents(Long target, List<Long> expectedSimilarStudents) throws Exception {
        UserNeighborhood neighborhood = neighborhoodStore.getStudentNeighborhood();
        List<Long> actualSimilarStudents = asList(toObject(neighborhood.getUserNeighborhood(target)));

        Assert.assertThat(actualSimilarStudents, CoreMatchers.equalTo(expectedSimilarStudents));
    }
}