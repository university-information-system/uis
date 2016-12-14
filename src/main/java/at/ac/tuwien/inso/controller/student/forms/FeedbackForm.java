package at.ac.tuwien.inso.controller.student.forms;

import javax.validation.constraints.*;

public class FeedbackForm {

    private Long course;

    private boolean like;

    @Size(max = 1024)
    private String suggestions;

    protected FeedbackForm() {

    }

    public FeedbackForm(Long course, boolean like, String suggestions) {
        this.course = course;
        this.like = like;
        this.suggestions = suggestions;
    }

    public Long getCourse() {
        return course;
    }

    public FeedbackForm setCourse(Long course) {
        this.course = course;
        return this;
    }

    public Boolean isLike() {
        return like;
    }

    public FeedbackForm setLike(boolean like) {
        this.like = like;
        return this;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public FeedbackForm setSuggestions(String suggestions) {
        this.suggestions = suggestions;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedbackForm that = (FeedbackForm) o;

        if (like != that.like) return false;
        if (course != null ? !course.equals(that.course) : that.course != null) return false;
        return suggestions != null ? suggestions.equals(that.suggestions) : that.suggestions == null;

    }

    @Override
    public int hashCode() {
        int result = course != null ? course.hashCode() : 0;
        result = 31 * result + (like ? 1 : 0);
        result = 31 * result + (suggestions != null ? suggestions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FeedbackForm{" +
                "course=" + course +
                ", like=" + like +
                ", suggestions='" + suggestions + '\'' +
                '}';
    }
}
