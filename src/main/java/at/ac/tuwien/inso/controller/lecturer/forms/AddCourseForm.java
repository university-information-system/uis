package at.ac.tuwien.inso.controller.lecturer.forms;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Tag;

public class AddCourseForm {

    private Course course;
    private ArrayList<String> tags = new ArrayList<>();

    private List<AddCourseTag> activeAndInactiveTags = new ArrayList<>();

    protected AddCourseForm() {
    }

    public AddCourseForm(Course course) {
        this.course = course;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
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

    public void setInitialActiveTags(List<Tag> initialActiveTags) {
        activeAndInactiveTags.stream()
                .filter(tag -> initialActiveTags.contains(tag.getTag()))
                .forEach(tag -> tag.setActive(true));
    }
}
