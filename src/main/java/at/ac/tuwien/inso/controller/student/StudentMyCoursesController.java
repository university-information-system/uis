package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.study_progress.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;

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
