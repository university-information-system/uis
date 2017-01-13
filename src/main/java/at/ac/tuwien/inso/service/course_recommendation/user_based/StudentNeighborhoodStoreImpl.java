package at.ac.tuwien.inso.service.course_recommendation.user_based;

import at.ac.tuwien.inso.service.student_subject_prefs.*;
import org.apache.mahout.cf.taste.common.*;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.similarity.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.util.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

import static org.apache.mahout.cf.taste.impl.model.GenericDataModel.*;

@Service
public class StudentNeighborhoodStoreImpl implements StudentNeighborhoodStore {

    private static final Logger log = LoggerFactory.getLogger(StudentNeighborhoodStoreImpl.class);

    private static final String STUDENT_NEIGHBORHOOD_CACHE_NAME = "student-neighborhood";
    private static final String STUDENT_NEIGHBORHOOD_CACHE_EVICT_CRON = "0 0 0 * * 0"; // Every Sunday at midnight

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Cacheable(STUDENT_NEIGHBORHOOD_CACHE_NAME)
    public UserNeighborhood getStudentNeighborhood() {
        DataModel model = buildDataModel();
        UserSimilarity similarity = buildSimilarityIndex(model);

        return new ThresholdUserNeighborhood(0.3, similarity, model);
    }

    private DataModel buildDataModel() {
        log.info("Building student subject preference data model");

        Query allQuery = new Query();
        try (CloseableIterator<StudentSubjectPreference> iterator = mongoTemplate.stream(allQuery, StudentSubjectPreference.class)) {
            return new GenericDataModel(toDataMap(extractRawStudentPreferenceData(iterator), true));
        }
    }

    private FastByIDMap<Collection<Preference>> extractRawStudentPreferenceData(CloseableIterator<StudentSubjectPreference> iterator) {
        FastByIDMap<Collection<Preference>> rawData = new FastByIDMap<>();

        iterator.forEachRemaining(it -> {
            Collection<Preference> studentPreferences;
            if (rawData.containsKey(it.getStudentId())) {
                studentPreferences = rawData.get(it.getStudentId());
            } else {
                studentPreferences = new ArrayList<>();
                rawData.put(it.getStudentId(), studentPreferences);
            }

            studentPreferences.add(new GenericPreference(it.getStudentId(), it.getSubjectId(), it.getPreferenceValue().floatValue()));
        });

        return rawData;
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
