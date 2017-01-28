package at.ac.tuwien.inso.service.course_recommendation.user_based;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang.ArrayUtils.toObject;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;

import at.ac.tuwien.inso.service.student_subject_prefs.StudentSubjectPreference;

@RunWith(MockitoJUnitRunner.class)
public class StudentNeighborhoodStoreImplTest {

    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private StudentNeighborhoodStore cacheStudentNeighborhoodStore;

    @InjectMocks
    private StudentNeighborhoodStoreImpl neighborhoodStore;

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

    @Test
    public void testRebuildOfStudentNeighborhood() throws Exception {
        neighborhoodStore.rebuildStudentNeighborhood();

        verify(cacheStudentNeighborhoodStore).getStudentNeighborhood();
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