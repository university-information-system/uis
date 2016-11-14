package at.ac.tuwien.inso.controller.lecturer.forms;

import at.ac.tuwien.inso.entity.Tag;

public class AddCourseTag {

    private Tag tag;
    private boolean active;

    public AddCourseTag (Tag tag, boolean active) {
        this.tag = tag;
        this.active = active;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
