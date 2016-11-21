package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@RequestMapping("/lecturer/addCourse")
public class LecturerAddCourseController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TagService tagService;

    @ModelAttribute("subject")
    private Subject getSubject(@RequestParam("subjectId") Long subjectId) {
        return subjectService.findOne(subjectId);
    }

    @ModelAttribute("addCourseForm")
    private AddCourseForm getAddCourseForm(@RequestParam("subjectId") Long subjectId) {
        AddCourseForm form = new AddCourseForm(
                new Course(getSubject(subjectId), semesterService.getCurrentSemester())
        );
        form.setInitialTags(tagService.findAll());
        return form;
    }


    @GetMapping
    private String getAddCoursesPage(@RequestParam("subjectId") Long subjectId) {
        return "lecturer/addCourse";
    }

    @PostMapping
    private String createCourse(@ModelAttribute AddCourseForm form,
                                RedirectAttributes redirectAttributes) {
        Course course = courseService.saveCourse(form);

        redirectAttributes.addFlashAttribute("createdCourse", course);
        return "redirect:/lecturer/courses";
    }

}
