package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lecturer/course-details")
public class LecturerCourseDetailsController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private GradeService gradeService;

    @GetMapping
    private String getCourseDetails(@RequestParam("courseId") Long courseId, Model model) {
        model.addAttribute("course", courseService.findOne(courseId));
        return "lecturer/course-details";
    }

    @GetMapping("registrations")
    private String getCourseRegistrations(@RequestParam("courseId") Long courseId, Model model) {
        Course course = courseService.findOne(courseId);
        model.addAttribute("course", course);
        model.addAttribute("students", course.getStudents());
        return "lecturer/course-registrations";
    }

    @GetMapping("issued-grades")
    private String getIssuedGrades(@RequestParam("courseId") Long courseId, Model model) {
        Course course = courseService.findOne(courseId);
        model.addAttribute("course", course);
        model.addAttribute("grades", gradeService.getGradesForCourseOfLoggedInLecturer(courseId));
        return "lecturer/issued-grades";
    }
}
