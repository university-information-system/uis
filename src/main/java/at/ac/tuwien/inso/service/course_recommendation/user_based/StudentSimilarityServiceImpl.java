package at.ac.tuwien.inso.service.course_recommendation.user_based;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.repository.StudentRepository;

@Service
public class StudentSimilarityServiceImpl implements StudentSimilarityService {

    @Autowired
    private StudentNeighborhoodStore studentNeighborhoodStore;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> getSimilarStudents(Student student) {
        UserNeighborhood userNeighborhood = studentNeighborhoodStore.getStudentNeighborhood();
        long[] userIds;
        try {
            userIds = userNeighborhood.getUserNeighborhood(student.getId());
        } catch (TasteException e) {
            return Collections.emptyList();
        }

        ArrayList<Student> students = new ArrayList<>();
        Arrays.stream(userIds).forEach(id -> students.add(studentRepository.findOne(id)));

        return students;
    }
}
