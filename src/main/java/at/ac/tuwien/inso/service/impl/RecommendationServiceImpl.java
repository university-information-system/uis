package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.utils.TagFrequency;
import at.ac.tuwien.inso.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            if (student.getCourses().contains(course)) {
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
