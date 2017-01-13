package at.ac.tuwien.inso.service.course_recommendation.user_based;

import org.apache.mahout.cf.taste.neighborhood.*;

public interface StudentNeighborhoodStore {

    UserNeighborhood getStudentNeighborhood();
}
