package at.ac.tuwien.inso.dto_tests;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import org.junit.Test;

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
	
	@Test
	public void testEqualsNoId() {
		assertFalse(getWS2016().equals(getWS2016WithoutId()));
	}
}
