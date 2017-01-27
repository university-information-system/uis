package at.ac.tuwien.inso.entity;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Subject {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private BigDecimal ects;

	@ManyToMany
	private List<Lecturer> lecturers = new ArrayList<>();

	public Subject() {
	}

	public Subject(String name, BigDecimal ects) {
		this.name = name;
		this.ects = ects;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getEcts() {
		return ects;
	}

	public void setEcts(BigDecimal ects) {
		this.ects = ects;
	}

	@JsonIgnore
	public List<Lecturer> getLecturers() {
		return unmodifiableList(lecturers);
	}

	public Subject addLecturers(Lecturer... lecturers) {
		this.lecturers.addAll(asList(lecturers));
		return this;
	}

	public void removeLecturers(Lecturer... lecturers) {
		this.lecturers.removeAll(asList(lecturers));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Subject subject = (Subject) o;

		if (id != null ? !id.equals(subject.id) : subject.id != null) return false;
		if (!name.equals(subject.name)) return false;
		return ects.equals(subject.ects);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = name != null ? 31 * result + name.hashCode() : 0;
		result = ects != null ? 31 * result + ects.hashCode(): 0;
		return result;
	}

	@Override
	public String toString() {
		return "Subject{" +
				"id=" + id +
				", name='" + name + '\'' +
				", ects=" + ects +
				", lecturers=" + lecturers +
				'}';
	}

}
