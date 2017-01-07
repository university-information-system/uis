package at.ac.tuwien.inso.dto_tests;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import org.junit.Test;

import at.ac.tuwien.inso.dto.SemesterDto;

@RunWith(MockitoJUnitRunner.class)
public class SemesterDtoTests {

	private SemesterDto getA(){
		SemesterDto a = new SemesterDto("A");
		a.setId(1L);
		return a;
	}
	
	private SemesterDto getB(){
		SemesterDto b = new SemesterDto("B");
		b.setId(2L);
		return b;
	}
	
	private SemesterDto getANoId(){
		SemesterDto a = new SemesterDto("A");
		return a;
	}
	
	@Test
	public void testSemesterDtoEqualsisEqual(){
		assertTrue(getA().equals(getA()));
	}
	
	@Test
	public void testSemesterDtoEqualsisNotEqual(){
		assertFalse(getA().equals(getB()));
	}
	
	@Test
	public void testSemesterDtoisEqual(){
		assertFalse(getA().equals(getANoId()));
	}
}
