package at.ac.tuwien.inso.dto;

public class SemesterDto {

	private Long id;

	private String label;

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
		return "Semester{" +
				"id=" + id +
				", label='" + label + '\'' +
				'}';
	}	
}
