package at.ac.tuwien.inso.entity;


import javax.persistence.*;
import javax.validation.constraints.*;

@Embeddable
public class Mark implements Comparable<Mark> {

    public static final Mark EXCELLENT = new Mark(1);
    public static final Mark GOOD = new Mark(2);
    public static final Mark SATISFACTORY = new Mark(3);
    public static final Mark SUFFICIENT = new Mark(4);
    public static final Mark FAILED = new Mark(5);

    @Min(1)
    @Max(5)
    private int mark;

    public void setMark(int mark) {
        this.mark = mark;
    }

    public Mark() {

    }

    private Mark(int mark) {
        this.mark = mark;
    }

    public boolean isPositive() {
        return !equals(FAILED);
    }

    public int getMark() {
        return mark;
    }

    @Override
    public int compareTo(Mark mark) {
        return mark.getMark() - this.mark;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mark mark1 = (Mark) o;

        return mark == mark1.mark;

    }

    @Override
    public int hashCode() {
        return mark;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "mark=" + mark +
                '}';
    }

}
