package at.ac.tuwien.inso.service.course_recommendation.user_based;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.repository.StudentRepository;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentSimilarityServiceTest {

    @Mock
    private StudentNeighborhoodStore studentNeighborhoodStore;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentSimilarityServiceImpl studentSimilarityService;

    private Student student = mock(Student.class);
    private UserNeighborhood userNeighborhood = mock(UserNeighborhood.class);

    private List<Student> students = asList(
            mock(Student.class),
            mock(Student.class),
            mock(Student.class)
    );

    @Test
    public void verifySimilarStudentsForAStudentThatHasNoSimilarUsers() throws Exception {
        long studentId = 0;
        long[] studentIds = {};

        when(studentNeighborhoodStore.getStudentNeighborhood()).thenReturn(userNeighborhood);
        when(userNeighborhood.getUserNeighborhood(studentId)).thenReturn(studentIds);

        List<Student> similarStudents = studentSimilarityService.getSimilarStudents(student);

        assertEquals(Collections.emptyList(), similarStudents);
    }

    @Test
    public void verifySimilarStudentsForAStudentThatHasSimilarUsers() throws Exception {
        long studentId = 0;
        long[] studentIds = {1, 2, 3};

        when(studentNeighborhoodStore.getStudentNeighborhood()).thenReturn(userNeighborhood);
        when(userNeighborhood.getUserNeighborhood(studentId)).thenReturn(studentIds);
        when(studentRepository.findOne(studentIds[0])).thenReturn(students.get(0));
        when(studentRepository.findOne(studentIds[1])).thenReturn(students.get(1));
        when(studentRepository.findOne(studentIds[2])).thenReturn(students.get(2));

        List<Student> similarStudents = studentSimilarityService.getSimilarStudents(student);

        assertEquals(students, similarStudents);
    }
}