package at.ac.tuwien.inso.controller.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import at.ac.tuwien.inso.controller.lecturer.forms.AddCourseForm;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.SemesterService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.TagService;

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
        return subjectService.getSubjectById(subjectId);
    }

    @ModelAttribute("addCourseForm")
    private AddCourseForm getAddCourseForm(@RequestParam("subjectId") Long subjectId) {
        AddCourseForm form = new AddCourseForm(
                new Course(getSubject(subjectId), semesterService.getCurrentSemester())
        );
        form.setInitialTags(tagService.getAllTags());
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
