package at.ac.tuwien.inso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.*;
import java.util.ArrayList;
import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;

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

	@ManyToMany
	private List<Subject> requiredSubjects = new ArrayList<>();

	public Subject() {
	}

	public Subject(String name, BigDecimal ects) {
		this.name = name;
		this.ects = ects;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getEcts() {
		return ects;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEcts(BigDecimal ects) {
		this.ects = ects;
	}

	@JsonIgnore
	public List<Lecturer> getLecturers() {
		return unmodifiableList(lecturers);
	}

	@JsonIgnore
	public List<Subject> getRequiredSubjects() {
		return requiredSubjects;
	}

	public void addLecturers(Lecturer... lecturers) {
		this.lecturers.addAll(asList(lecturers));
	}

	public void removeLecturers(Lecturer... lecturers) {
		this.lecturers.removeAll(asList(lecturers));
	}

	public void addRequiredSubjects(Subject... subjects) {
		this.requiredSubjects.addAll(asList(subjects));
	}

	public void removeRequiredSubjects(Subject... subjects) {
		this.requiredSubjects.removeAll(asList(subjects));
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
				", requiredSubjects=" + requiredSubjects +
				'}';
	}
}
