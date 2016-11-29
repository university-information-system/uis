package at.ac.tuwien.inso.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.service.GradeService;

@Controller
@RequestMapping("/student/grades")
public class StudentGradesController {

    @Autowired
    private GradeService gradeService;

    @ModelAttribute("grades")
    private List<Grade> getGrades() {
        return gradeService.getGradesForLoggedInStudent();
    }

    @GetMapping
    private String getPage() {
        return "student/grades";
    }

}
