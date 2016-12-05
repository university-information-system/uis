package at.ac.tuwien.inso.service.study_progress.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.study_progress.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;

@Service
public class StudyProgressServiceImpl implements StudyProgressService {

    @Autowired
    private CourseService courseService;

    @Override
    @Transactional(readOnly = true)
    public StudyProgress studyProgressFor(Student student) {
        List<Course> courses = courseService.findCourseForCurrentSemesterWithName("");

        return new StudyProgress(
                new Semester("WS2016/17"),
                asList(
                        new SemesterProgress(new Semester("WS2016/17"), asList(
                                new CourseRegistration(courses.get(0)),
                                new CourseRegistration(courses.get(1), CourseRegistrationState.needs_feedback)
                        )),
                        new SemesterProgress(new Semester("SS2016"), singletonList(
                                new CourseRegistration(courses.get(2), CourseRegistrationState.complete_ok)
                        )),
                        new SemesterProgress(new Semester("WS2015/16"), emptyList()),
                        new SemesterProgress(new Semester("SS2014"), asList(
                                new CourseRegistration(courses.get(3), CourseRegistrationState.needs_feedback),
                                new CourseRegistration(courses.get(4), CourseRegistrationState.complete_not_ok),
                                new CourseRegistration(courses.get(5), CourseRegistrationState.complete_ok)
                        ))
                )
        );
    }
}
