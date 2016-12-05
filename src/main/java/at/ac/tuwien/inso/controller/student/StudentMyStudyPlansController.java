package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/my-studyplans")
public class StudentMyStudyPlansController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String listStudyPlansForStudent() {
        return "/student/my-studyplans";
    }

    @ModelAttribute("studyPlanRegistrations")
    private Iterable<StudyPlanRegistration> getStudyPlans() {
        Student student = studentService.findOne(userAccountService.getCurrentLoggedInUser());
        return student.getStudyplans();
    }


}
