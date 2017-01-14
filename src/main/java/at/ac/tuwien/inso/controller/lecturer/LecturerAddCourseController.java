package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.impl.Messages;
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

    @Autowired
    private Messages messages;

    @ModelAttribute("subject")
    private Subject getSubject(@RequestParam("subjectId") Long subjectId) {
        return subjectService.findOne(subjectId);
    }

    @ModelAttribute("addCourseForm")
    private AddCourseForm getAddCourseForm(@RequestParam("subjectId") Long subjectId) {
        Semester semester = semesterService.getOrCreateCurrentSemester().toEntity();

        // TODO use DTO
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
        Course course = courseService.saveCourse(form);

        redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("lecturer.course.add.success", course.getSubject().getName()));
        return "redirect:/lecturer/courses";
    }

}
