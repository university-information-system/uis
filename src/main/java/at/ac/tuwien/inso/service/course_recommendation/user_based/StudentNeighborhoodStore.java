package at.ac.tuwien.inso.service.course_recommendation.user_based;

import org.apache.mahout.cf.taste.neighborhood.*;

public interface StudentNeighborhoodStore {

    /**
     * Computes and returns the neighbourhood based on the similarity
     * index for students.
     * <p>
     * Implementors of this may also cache the similarity index to avoid
     * its expensive computation. Therefore, it is not guaranteed that this
     * method returns an up-to-date view of the user similarities, as reflected
     * by the most recent student activities.
     *
     * @return the similarity neighborhood for students
     */
    UserNeighborhood getStudentNeighborhood();
}
