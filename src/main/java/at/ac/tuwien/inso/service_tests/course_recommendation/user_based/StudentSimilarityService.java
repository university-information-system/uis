package at.ac.tuwien.inso.service_tests.course_recommendation.user_based;

import java.util.List;

import at.ac.tuwien.inso.entity.Student;

public interface StudentSimilarityService {

    List<Student> getSimilarStudents(Student student);
}
