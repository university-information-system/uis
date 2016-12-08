package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.repository.utils.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> recommendCourses(Student student) {
        List<TagFrequency> tagFrequencyForStudent = studentRepository.computeTagsFrequencyFor(student);
        List<Course> coursesByCurrentSemester = courseRepository.findAllByCurrentSemesterWithTags();
        Map<Long, List<Course>> coursesToRecommendMap = new TreeMap<>(Collections.reverseOrder());
        List<Course> coursesToRecommendList = new ArrayList<>();
        List<Course> studentCourses = courseRepository.findAllForStudent(student);

        long score;
        for (Course course : coursesByCurrentSemester) {
            score = 0;
            for (TagFrequency tagFrequency : tagFrequencyForStudent) {
                for (Tag tag : course.getTags()) {
                    if (tagFrequency.getTag().equals(tag)) {
                        score += tagFrequency.getFrequency();
                    }
                }
            }
            if (studentCourses.contains(course)) {
                continue;
            }
            if (!coursesToRecommendMap.containsKey(score)) {
                coursesToRecommendMap.put(score, new ArrayList<>());
            }

            coursesToRecommendMap.get(score).add(course);
        }

        for (long key : coursesToRecommendMap.keySet()) {
            coursesToRecommendList.addAll(coursesToRecommendMap.get(key));
        }

        return coursesToRecommendList;
    }
}
