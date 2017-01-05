package at.ac.tuwien.inso.service_tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import com.lowagie.text.List;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.impl.SubjectServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class SubjectServiceTests {

	@Mock
	private CourseService courseService;
	
	@Mock
	private SubjectRepository subjectRepository;
	
	@InjectMocks
    private SubjectService subjectService = new SubjectServiceImpl();
	
	@Test
	public void testRemoveWithValidInput(){
		Subject s = new Subject("testSubject", new BigDecimal(11));
		ArrayList<Course> toReturn = new ArrayList<Course>();
		when(courseService.findCoursesForSubject(s)).thenReturn(toReturn);

		assertTrue(subjectService.remove(s));

		
	}
	
	@Test(expected = ValidationException.class)
	public void testRemoveWithNullInputThrowsException(){
		subjectService.remove(null);
		
	}
	
	@Test(expected = ValidationException.class)
	public void testRemoveWithContaingCoursesInput(){
		Subject s = new Subject("testSubject", new BigDecimal(11));
		ArrayList<Course> toReturn = new ArrayList<Course>();
		toReturn.add(new Course(s, new Semester("oldSemester")));
		when(courseService.findCoursesForSubject(s)).thenReturn(toReturn);

        subjectService.remove(s);

	}
}
