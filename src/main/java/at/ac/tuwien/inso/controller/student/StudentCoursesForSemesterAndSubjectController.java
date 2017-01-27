package at.ac.tuwien.inso.controller.student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.course_recommendation.RecommendationService;

@Controller
@RequestMapping("/student/courses/semester/subject")
public class StudentCoursesForSemesterAndSubjectController {

    private static final Logger log = LoggerFactory.getLogger(StudentCoursesForSemesterAndSubjectController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String courses(@RequestParam Long subjectId, Model model) {

        log.info("getting courses for subject " + subjectId);

        Subject subject = subjectService.findOne(subjectId);
        List<Course> courses = courseService.findCoursesForSubjectAndCurrentSemester(subject);

        if (courses.size() == 1) {
            log.info("Subject only has one course this semester. Redirecting to course");
            return "redirect:/student/courses/" + courses.get(0).getId();
        }

        model.addAttribute("coursesForSemesterAndSubject", courses);
        return "/student/courses-for-subject";
    }


}
