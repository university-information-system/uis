package at.ac.tuwien.inso.dto;

import at.ac.tuwien.inso.entity.Semester;

//finished transformation on 8.1.
public class SemesterDto {

	private Long id;

	private String label;


    public SemesterDto(String label) {
        this.label = label;
    }
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SemesterDto semester = (SemesterDto) o;

		if (id != null ? !id.equals(semester.id) : semester.id != null) return false;
		return label != null ? label.equals(semester.label) : semester.label == null;

	}

	@Override
	public String toString() {
		return "SemesterDto{" +
				"id=" + id +
				", label='" + label + '\'' +
				'}';
	}	
	
	/**
	 * sets the label to a new Semester instance. The Id will not be persisted.
	 * @return
	 */
	public Semester toEntity(){
		Semester semesterEntity = new Semester(label);
		semesterEntity.setId(id);
		return semesterEntity;
	}

}
