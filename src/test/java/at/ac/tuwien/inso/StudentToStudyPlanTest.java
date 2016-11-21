package at.ac.tuwien.inso;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.*;

import java.math.*;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentToStudyPlanTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
	private StudyPlanService studyPlanService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private StudyPlanRepository studyplanRepository;
	
	@Autowired
	private SemesterRepository semesterRepository;

    @Test
    public void isStudyPlanAddedToStudent() throws Exception {
    	Semester semester = semesterRepository.save(new Semester("WS2016"));
       Student newStudent = studentRepository.save(new Student("TestUser", "a@c.com"));
       
       StudyPlan sp = studyplanRepository.save(new StudyPlan("TestStudyPlan", new EctsDistribution(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN)));
    	
        mockMvc.perform(
                get("/admin/registerToStudyplan/")
                .param("studentId", newStudent.getId()+"")
                .param("studyPlanId", sp.getId()+"")
                .with(user("admin").roles("ADMIN"))
        ).andExpect(
                redirectedUrl("/admin/users/"+newStudent.getId())
        ).andExpect(it -> {
            Student s = studentService.findOne(newStudent.getId());
            assertEquals(sp, s.getStudyplans().get(0).getStudyplan());
        });
    }

}
