package at.ac.tuwien.inso.dto;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;

import static java.util.Calendar.SECOND;

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

	/**
	 * The start date of the semester
	 *
	 * Calculated from automatic start of the semester type.
	 */
	public Calendar getStart() {
		Calendar calendar = new GregorianCalendar(getYear(), getType().getStartMonth(), getType().getStartDay());
		return calendar;
	}

    /**
     * SemesterDto that could be the current one
     *
     * Does not come from the DB, but only is calculated. Needed to find out, if the current
     * semester is outdated.
     */
	public static SemesterDto calculateCurrentSemester(Calendar now) {
        int currentYear = now.get(Calendar.YEAR);

        List<SemesterDto> allSemesters = new LinkedList<>();

        int[] possibleYears = {currentYear - 1, currentYear, currentYear + 1};

        // Create a list of all possible semesters in those 3 years
        for (int year : possibleYears) {
            for (SemesterType type : SemesterType.values()) {
                allSemesters.add(new SemesterDto(year, type));
            }
        }

        SemesterDto possibleCurrent = allSemesters
                .stream()
                .filter(s -> s.isStartInPast(now)) // Current semesters that start in the future are not possible
                .sorted(Comparator.comparing(SemesterDto::getStart).reversed())
                .findFirst()
                .get();

        return possibleCurrent;
    }

    /**
     * If the given Semester is also the current Semester
     *
     * @param now date to compare with (needed for testing)
     */
    public boolean isCurrent(Calendar now) {
        SemesterDto calculated = calculateCurrentSemester(now);
        return getYear() == calculated.getYear() && getType() == calculated.getType();
    }

    /**
     * If the semester started in the past
     * @param now date to compare with (needed for testing)
     */
    public boolean isStartInPast(Calendar now) {
        return getStart().before(now);
    }

    /**
     * The semester following after this
     */
    public SemesterDto nextSemester() {
        int currentYear = this.getYear();

        List<SemesterDto> allSemesters = new LinkedList<>();

        int[] possibleYears = {currentYear, currentYear + 1};

        // Create a list of all possible semesters in those 3 years
        for (int year : possibleYears) {
            for (SemesterType type : SemesterType.values()) {
                allSemesters.add(new SemesterDto(year, type));
            }
        }

        // Add a second, so that the same semester does not get propsed again
        Calendar startLimit = getStart();
        startLimit.add(Calendar.SECOND, 1);

        SemesterDto next = allSemesters
                .stream()
                .filter(s -> ! s.isStartInPast(startLimit))
                .sorted(Comparator.comparing(SemesterDto::getStart))
                .findFirst()
                .get();

        return next;
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

    @Override
    public String toString() {
        return "SemesterDto{" + "id=" + id + ", year=" + year + ", type=" + type + '}';
    }
}
