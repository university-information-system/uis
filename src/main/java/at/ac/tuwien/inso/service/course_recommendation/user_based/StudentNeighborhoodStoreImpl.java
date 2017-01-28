package at.ac.tuwien.inso.service.course_recommendation.user_based;

import static org.apache.mahout.cf.taste.impl.model.GenericDataModel.toDataMap;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import at.ac.tuwien.inso.service.student_subject_prefs.StudentSubjectPreference;

@Service
public class StudentNeighborhoodStoreImpl implements StudentNeighborhoodStore {

    private static final Logger log = LoggerFactory.getLogger(StudentNeighborhoodStoreImpl.class);

    private static final String STUDENT_NEIGHBORHOOD_CACHE_NAME = "student-neighborhood";
    private static final String STUDENT_NEIGHBORHOOD_CACHE_EVICT_CRON = "0 0 0 * * 0"; // Every Sunday at midnight

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StudentNeighborhoodStore studentNeighborhoodStore; // needed for the cache proxy, to rebuild cache

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
    public void rebuildStudentNeighborhood() {
        log.info("Cleared student neighborhood cache");

        studentNeighborhoodStore.getStudentNeighborhood(); // causes the data model to be rebuilt and cached
    }
}
