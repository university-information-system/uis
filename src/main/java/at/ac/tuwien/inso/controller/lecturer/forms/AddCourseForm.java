package at.ac.tuwien.inso.controller.lecturer.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Tag;

public class AddCourseForm {

    private Course course;

    private List<AddCourseTag> activeAndInactiveTags = new ArrayList<>();

    protected AddCourseForm() {
    }

    public AddCourseForm(Course course) {
        this.course = course;

    }

    public AddCourseForm setInitialTags(List<Tag> tags) {
        tags.forEach(tag -> activeAndInactiveTags.add(new AddCourseTag(tag, false)));
        return this;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<AddCourseTag> getActiveAndInactiveTags() {
        return activeAndInactiveTags;
    }

    public void setActiveAndInactiveTags(List<AddCourseTag> activeAndInactiveTags) {
        this.activeAndInactiveTags = activeAndInactiveTags;
    }
}
