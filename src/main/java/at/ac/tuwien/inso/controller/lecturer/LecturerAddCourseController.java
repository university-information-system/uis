package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.exception.ValidationException;
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
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.SemesterService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.TagService;
import at.ac.tuwien.inso.service.impl.Messages;

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

    @Autowired
    private Messages messages;

    @ModelAttribute("subject")
    private Subject getSubject(@RequestParam("subjectId") Long subjectId) {
        return subjectService.findOne(subjectId);
    }

    @ModelAttribute("addCourseForm")
    private AddCourseForm getAddCourseForm(@RequestParam("subjectId") Long subjectId) {
        Semester semester = semesterService.getOrCreateCurrentSemester().toEntity();

        Course course = new Course(getSubject(subjectId), semester);

        AddCourseForm form = new AddCourseForm(course);
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
        try {
            Course course = courseService.saveCourse(form);
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("lecturer.course.add.success", course.getSubject().getName()));
        }
        catch(ValidationException e) {
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", e.getMessage());
        }


        return "redirect:/lecturer/courses";
    }

}
