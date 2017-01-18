package at.ac.tuwien.inso.dto_tests;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.SemesterType;

@RunWith(MockitoJUnitRunner.class)
public class SemesterDtoTests {

	private SemesterDto getWS2016WithoutId() {
		return new SemesterDto(2016, SemesterType.WinterSemester);
	}

	private SemesterDto getWS2016(){
		SemesterDto a = getWS2016WithoutId();
		a.setId(1L);
		return a;
	}
	
	private SemesterDto getSS2015(){
		SemesterDto b = new SemesterDto(2015, SemesterType.SummerSemester);
		b.setId(2L);
		return b;
	}

	
	@Test
	public void testEquals() {
		assertTrue(getWS2016().equals(getWS2016()));
	}
	
	@Test
	public void testEqualsDifferntValues() {
		assertFalse(getWS2016().equals(getSS2015()));
	}

	@Ignore // ignore atm because difficult to write integration tests when using the id to check equality
	@Test
	public void testEqualsNoId() {
		assertFalse(getWS2016().equals(getWS2016WithoutId()));
	}

	@Test
	public void testIsStartInPast() {
        Calendar before = new GregorianCalendar(2000, 1, 1);
        Calendar after = new GregorianCalendar(2020, 3, 3);

        SemesterDto semester = new SemesterDto(2010, SemesterType.SummerSemester);

        assertFalse(semester.isStartInPast(before));
        assertTrue(semester.isStartInPast(after));
    }

	@Test
	public void testCalculateCurrentSemesterSS() {
		Calendar currentDay = new GregorianCalendar(2016, 06, 06);

		SemesterDto expected = new SemesterDto(2016, SemesterType.SummerSemester);

		SemesterDto actual = SemesterDto.calculateCurrentSemester(currentDay);

		assertEquals(expected, actual);
	}

	@Test
	public void testCalculateCurrentSemesterWS() {
		Calendar currentDay = new GregorianCalendar(2000, 01, 07);

		SemesterDto expected = new SemesterDto(1999, SemesterType.WinterSemester);

		SemesterDto actual = SemesterDto.calculateCurrentSemester(currentDay);

		assertEquals(expected, actual);
	}

    @Test
    public void testIsCurrent() {
        Calendar now = new GregorianCalendar(2016, 1, 1);

        SemesterDto semester = new SemesterDto(2015, SemesterType.WinterSemester);

        assertTrue(semester.isCurrent(now));
    }

    @Test
    public void testNotIsCurrent() {
        Calendar now = new GregorianCalendar(2000, 7, 7);

        SemesterDto semester = new SemesterDto(2000, SemesterType.WinterSemester);

        assertFalse(semester.isCurrent(now));
    }

    @Test
    public void testNotIsCurrentOtherYear() {
        Calendar now = new GregorianCalendar(2000, 7, 7);

        SemesterDto semester = new SemesterDto(2010, SemesterType.SummerSemester);

        assertFalse(semester.isCurrent(now));
    }

    @Test
    public void testNextPossibleSemester() {
	    SemesterDto semester = new SemesterDto(2016, SemesterType.WinterSemester);

	    SemesterDto expected = new SemesterDto(2017, SemesterType.SummerSemester);

	    SemesterDto actual = semester.nextSemester();

	    assertEquals(expected, actual);
    }

}
