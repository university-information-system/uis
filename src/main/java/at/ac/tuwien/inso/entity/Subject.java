package at.ac.tuwien.inso.entity;

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

	protected Subject() {
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

	public List<Lecturer> getLecturers() {
		return unmodifiableList(lecturers);
	}

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

		if (!id.equals(subject.id)) return false;
		if (!name.equals(subject.name)) return false;
		if (!ects.equals(subject.ects)) return false;
		if (!lecturers.equals(subject.lecturers)) return false;
		return requiredSubjects.equals(subject.requiredSubjects);

	}

	@Override
	public int hashCode() {
		if(id!=null){
		int result = id.hashCode();
			result = 31 * result + name.hashCode();
			result = 31 * result + ects.hashCode();
			result = 31 * result + lecturers.hashCode();
			result = 31 * result + requiredSubjects.hashCode();
	
			return result;
		}else{
			return -1;
		}
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
