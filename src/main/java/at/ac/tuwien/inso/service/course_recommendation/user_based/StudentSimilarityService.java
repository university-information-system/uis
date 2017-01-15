package at.ac.tuwien.inso.service.course_recommendation.user_based;

import at.ac.tuwien.inso.entity.Student;

import java.util.List;

public interface StudentSimilarityService {

    List<Student> getSimilarStudents(Student student);
}
