package at.ac.tuwien.inso.service.course_recommendation.user_based;

import org.apache.mahout.cf.taste.common.*;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.impl.model.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.similarity.*;
import org.slf4j.*;
import org.springframework.cache.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Service
public class StudentNeighborhoodStoreImpl implements StudentNeighborhoodStore {

    private static final Logger log = LoggerFactory.getLogger(StudentNeighborhoodStoreImpl.class);

    private static final String STUDENT_NEIGHBORHOOD_CACHE_NAME = "student-neighborhood";
    private static final String STUDENT_NEIGHBORHOOD_CACHE_EVICT_CRON = "0 0 * * 0"; // Every Sunday at midnight

    @Override
    @Cacheable(STUDENT_NEIGHBORHOOD_CACHE_NAME)
    public UserNeighborhood getStudentNeighborhood() {
        DataModel model = buildDataModel();
        UserSimilarity similarity = buildSimilarityIndex(model);

        return new ThresholdUserNeighborhood(0.3, similarity, model);
    }

    private DataModel buildDataModel() {
        log.info("Building student subject preference data model");
        //TODO: implement
        return new GenericDataModel(new FastByIDMap<>());
    }

    private UserSimilarity buildSimilarityIndex(DataModel model) {
        log.info("Building student subject similarity index");

        try {
            return new PearsonCorrelationSimilarity(model);
        } catch (TasteException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = STUDENT_NEIGHBORHOOD_CACHE_EVICT_CRON)
    @CacheEvict(value = STUDENT_NEIGHBORHOOD_CACHE_NAME, allEntries = true)
    public void clearStudentNeighborhoodCache() {
        log.info("Cleared student neighborhood cache");
    }
}
