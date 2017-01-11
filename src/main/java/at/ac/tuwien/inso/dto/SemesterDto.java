package at.ac.tuwien.inso.dto;

import java.util.Objects;

import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;

//finished transformation on 8.1.
public class SemesterDto {

	private Long id;

	private int year;

	private SemesterType type;

	public SemesterDto() {
	}

	public SemesterDto(int year, SemesterType type) {
		this.year = year;
		this.type = type;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return getYear() + " " + getType();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public SemesterType getType() {
		return type;
	}

	public void setType(SemesterType type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SemesterDto that = (SemesterDto) o;

		return getYear() == that.getYear() && Objects.equals(getId(), that.getId()) && getType() == that.getType();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getYear(), getType());
	}

	/**
	 * sets the label to a new Semester instance. The Id will not be persisted.
	 * @return
	 */
	public Semester toEntity(){
		Semester semesterEntity = new Semester(year, type);
		semesterEntity.setId(id);
		return semesterEntity;
	}

}
