package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.dto.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.impl.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

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
                            @RequestParam("mark") Integer mark) {

        GradeAuthorizationDTO dto = gradeService.getDefaultGradeAuthorizationDTOForStudentAndCourse(studentId, courseId);
        dto.getGrade().setMark(Mark.of(mark));
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
