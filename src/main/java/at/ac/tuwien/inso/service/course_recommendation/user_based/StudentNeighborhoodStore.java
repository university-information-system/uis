package at.ac.tuwien.inso.service.course_recommendation.user_based;

import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;

public interface StudentNeighborhoodStore {

    UserNeighborhood getStudentNeighborhood();
}
