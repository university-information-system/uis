package at.ac.tuwien.inso.repository.utils;

import at.ac.tuwien.inso.entity.Tag;

public class TagFrequency {

    private Tag tag;

    private long frequency;

    public TagFrequency(Tag tag, long frequency) {
        this.tag = tag;
        this.frequency = frequency;
    }

    public Tag getTag() {
        return tag;
    }

    public long getFrequency() {
        return frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagFrequency that = (TagFrequency) o;

        if (frequency != that.frequency) return false;
        return tag != null ? tag.equals(that.tag) : that.tag == null;

    }

    @Override
    public int hashCode() {
        int result = tag != null ? tag.hashCode() : 0;
        result = 31 * result + (int) (frequency ^ (frequency >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "TagFrequency{" +
                "tag=" + tag +
                ", frequency=" + frequency +
                '}';
    }
}
