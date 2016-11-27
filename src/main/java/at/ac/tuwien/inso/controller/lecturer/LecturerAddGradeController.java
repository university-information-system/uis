package at.ac.tuwien.inso.controller.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.service.GradeService;


@Controller
@RequestMapping(value = "/lecturer/addGrade")
public class LecturerAddGradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    public String getPage(@RequestParam("courseId") Long courseId,
                          @RequestParam("studentId") Long studentId,
                          Model model) {
        model.addAttribute("grade",
                gradeService.getDefaultGradeForStudentAndCourse(studentId, courseId));
        return "lecturer/addGrade";
    }

    @PostMapping
    public String saveGrade(@Valid @ModelAttribute("grade") Grade grade,
                            RedirectAttributes redirectAttributes) {
        grade = gradeService.saveNewGradeForStudentAndCourse(grade);
        redirectAttributes.addFlashAttribute("addedGrade", grade);
        return "redirect:/lecturer/courses";
    }

}
