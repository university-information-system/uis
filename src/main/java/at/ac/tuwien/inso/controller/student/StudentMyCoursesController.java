package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;

@Controller
@RequestMapping("/student/myCourses")
public class StudentMyCoursesController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String myCourses(Model model) {
        List<Course> courses = courseService.findCourseForCurrentSemesterWithName("");

        model.addAttribute("studyProgress", new StudyProgress(
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
                ))
        );

        return "student/my-courses";
    }

    public enum CourseRegistrationState {
        in_progress, needs_feedback, complete_ok, complete_not_ok
    }

    public class StudyProgress {

        private Semester currentSemester;

        private List<SemesterProgress> semestersProgress;

        public StudyProgress(Semester currentSemester, List<SemesterProgress> semestersProgress) {
            this.currentSemester = currentSemester;
            this.semestersProgress = semestersProgress;
        }

        public Semester getCurrentSemester() {
            return currentSemester;
        }

        public List<SemesterProgress> getSemestersProgress() {
            return semestersProgress;
        }
    }

    public class SemesterProgress {

        private Semester semester;

        private List<CourseRegistration> courseRegistrations;

        public SemesterProgress(Semester semester, List<CourseRegistration> courseRegistrations) {
            this.semester = semester;
            this.courseRegistrations = courseRegistrations;
        }

        public Semester getSemester() {
            return semester;
        }

        public List<CourseRegistration> getCourseRegistrations() {
            return courseRegistrations;
        }
    }

    public class CourseRegistration {

        private Course course;

        private CourseRegistrationState state;

        public CourseRegistration(Course course) {
            this(course, CourseRegistrationState.in_progress);
        }

        public CourseRegistration(Course course, CourseRegistrationState state) {
            this.course = course;
            this.state = state;
        }

        public Course getCourse() {
            return course;
        }

        public CourseRegistrationState getState() {
            return state;
        }
    }
}
