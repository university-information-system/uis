package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.LecturerService;

@Controller
@RequestMapping("/lecturer/courses")
public class LecturerCoursesController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private LecturerService lecturerService;
    @Autowired
    private TagService tagService;


    @ModelAttribute("allCourses")
    private List<Course> getAllCourses() {
        Lecturer lecturer = lecturerService.getLoggedInLecturer();
        return courseService.findCoursesForCurrentSemesterForLecturer(lecturer);
    }

    @GetMapping
    public String courses() {
        return "lecturer/courses";
    }

    @GetMapping("json/tags")
    @ResponseBody
    private List<String> getTagsJson() {
        return tagService.findAll().stream().map(Tag::getName).collect(Collectors.toList());
    }

    @GetMapping(value = "json/tags", params = "courseId")
    @ResponseBody
    private List<String> getTagsForCourseJson(@RequestParam("courseId") Long courseId) {
        Course course = courseService.findOne(courseId);
        return course.getTags().stream().map(Tag::getName).collect(Collectors.toList());
    }
}
