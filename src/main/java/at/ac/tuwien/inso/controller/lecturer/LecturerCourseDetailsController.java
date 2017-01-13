package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.dto.GradeAuthorizationDTO;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.FeedbackService;
import at.ac.tuwien.inso.service.GradeService;
import at.ac.tuwien.inso.service.impl.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lecturer/course-details")
public class LecturerCourseDetailsController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private Messages messages;

    @GetMapping
    private String getCourseDetails(@RequestParam("courseId") Long courseId, Model model) {
        Course course = courseService.findOne(courseId);
        model.addAttribute("course", course);
        model.addAttribute("studyPlans", courseService.getSubjectForStudyPlanList(course));
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

    @GetMapping("feedback")
    private String getCourseFeedback(@RequestParam("courseId") Long courseId, Model model) {
        Course course = courseService.findOne(courseId);
        model.addAttribute("course", course);
        model.addAttribute("feedbacks", feedbackService.findFeedbackForCourse(courseId));
        return "lecturer/course-feedback";
    }

    @PostMapping("addGrade")
    public String saveGrade(RedirectAttributes redirectAttributes,
                            @RequestParam("courseId") Long courseId,
                            @RequestParam("studentId") Long studentId,
                            @RequestParam("authCode") String authCode,
                            @RequestParam("mark") Long mark) {

        GradeAuthorizationDTO dto = gradeService.getDefaultGradeAuthorizationDTOForStudentAndCourse(studentId, courseId);
        dto.getGrade().getMark().setMark(Math.toIntExact(mark));
        dto.setAuthCode(authCode);
        Student student = dto.getGrade().getStudent();
        try {
            gradeService.saveNewGradeForStudentAndCourse(dto);
            String successMsg = messages.msg("lecturer.courses.registrations.issueCertificate.success", student.getName());
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", successMsg);
        }
        catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", e.getMessage());
            return "redirect:/lecturer/course-details/registrations?courseId=" + courseId;
        }

        return "redirect:/lecturer/course-details/issued-grades?courseId=" + courseId;
    }
}
