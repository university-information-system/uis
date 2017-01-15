package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.impl.Messages;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@RequestMapping("/lecturer/editCourse")
public class LecturerEditCourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private TagService tagService;

    @Autowired
    private Messages messages;

    @ModelAttribute("subject")
    private Subject getSubject(@RequestParam("courseId") Long courseId) {
        return courseService.findOne(courseId).getSubject();
    }

    @ModelAttribute("addCourseForm")
    private AddCourseForm getAddCourseForm(@RequestParam("courseId") Long courseId) {
        Course course = courseService.findOne(courseId);
        AddCourseForm form = new AddCourseForm(course);
        form.setInitialTags(tagService.findAll());
        form.setInitialActiveTags(course.getTags());
        return form;
    }


    @GetMapping
    private String getAddCoursesPage(@RequestParam("courseId") Long courseId) {
        return "lecturer/editCourse";
    }

    @PostMapping
    private String createCourse(@ModelAttribute AddCourseForm form,
                                RedirectAttributes redirectAttributes) {
        Course course = courseService.saveCourse(form);

        redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("lecturer.course.edit.success", course.getSubject().getName()));
        return "redirect:/lecturer/courses";
    }
    
    @GetMapping("/remove")
    private String removeCourse(@RequestParam("courseId") Long courseId, RedirectAttributes redirectAttributes){
    	
    	try{
    		courseService.remove(courseId);
    	}catch(ValidationException|BusinessObjectNotFoundException e){
    		redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("lecturer.course.remove.fail"));
            return "redirect:/lecturer/courses";
    	}
    	redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("lecturer.course.remove.success"));
    	return "redirect:/lecturer/courses";
    }

}
