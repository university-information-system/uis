package at.ac.tuwien.inso.controller.lecturer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.ac.tuwien.inso.controller.Constants;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.LecturerService;
import at.ac.tuwien.inso.service.TagService;

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
    
    @ModelAttribute("allCoursesForAllLecturers")
    private List<Course> getAllCoursesForAllLecturers() {
        return courseService.findAllCoursesForCurrentSemester();
    }
    
    @ModelAttribute("allCoursesForAllLecturersPagable")
    private Page<Course> getAllCoursesPageable(@RequestParam(value = "search", defaultValue = "") String search, @PageableDefault Pageable pageable) {
        if (pageable.getPageSize() > Constants.MAX_PAGE_SIZE) {
            pageable = new PageRequest(pageable.getPageNumber(), Constants.MAX_PAGE_SIZE);
        }

        return courseService.findCourseForCurrentSemesterWithName(search, pageable);
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
    
    @GetMapping("all")
    public String allCourses(){
    	return "lecturer/allcourses";
    }
}
