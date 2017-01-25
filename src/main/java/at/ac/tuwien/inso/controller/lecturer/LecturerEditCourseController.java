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

import at.ac.tuwien.inso.controller.lecturer.forms.AddCourseForm;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.TagService;
import at.ac.tuwien.inso.service.impl.Messages;

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
    private String getEditCoursePage(@RequestParam("courseId") Long courseId, Model model) {
        Course course = courseService.findOne(courseId);
        model.addAttribute("course", course);
        return "lecturer/editCourse";
    }

    @PostMapping
    private String updateCourse(@ModelAttribute AddCourseForm form,
                                RedirectAttributes redirectAttributes) {

        try {
            Course course = courseService.saveCourse(form);
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("lecturer.course.edit.success", course.getSubject().getName()));
        }
        catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", e.getMessage());
        }



        return "redirect:/lecturer/courses";
    }
    
    @PostMapping("/remove")
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
