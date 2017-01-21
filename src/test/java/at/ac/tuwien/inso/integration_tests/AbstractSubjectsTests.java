package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AbstractSubjectsTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected SubjectRepository subjectRepository;

    @Autowired
    protected LecturerRepository lecturerRepository;

    protected UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    protected Lecturer lecturer1 = new Lecturer("l0001", "Lecturer 1", "email", user1);
    protected Lecturer lecturer2 = new Lecturer("l0002", "Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER));
    protected Lecturer lecturer3 = new Lecturer("l0003", "Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER));
    protected Subject calculus = new Subject("Calculus", new BigDecimal(3.0));
    protected Subject sepm = new Subject("SEPM", new BigDecimal(6.0));
    protected Subject ase = new Subject("ASE", new BigDecimal(6.0));

    @Before
    public void setUp(){
        lecturerRepository.save(lecturer1);
        lecturerRepository.save(lecturer2);
        lecturerRepository.save(lecturer3);
    }




}
