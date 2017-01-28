package at.ac.tuwien.inso.service_tests.course_recommendation.user_based;

import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;

public interface StudentNeighborhoodStore {

    UserNeighborhood getStudentNeighborhood();
}
