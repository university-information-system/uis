package at.ac.tuwien.inso.service.course_recommendation.user_based;

import java.util.List;

import at.ac.tuwien.inso.entity.Student;

public interface StudentSimilarityService {

    List<Student> getSimilarStudents(Student student);
}
