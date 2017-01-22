package at.ac.tuwien.inso.controller.student;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.study_progress.StudyProgressService;

@Controller
@RequestMapping("/student/myCourses")
public class StudentMyCoursesController {

    private static final Logger log = LoggerFactory.getLogger(StudentMyCoursesController.class);

    @Autowired
    private StudyProgressService studyProgressService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String myCourses(Model model, Principal principal) {
        log.debug("Showing study progress for student with username '{}'", principal.getName());

        Student student = studentService.findByUsername(principal.getName());

        model.addAttribute("studyProgress", studyProgressService.studyProgressFor(student));

        return "student/my-courses";
    }
}
