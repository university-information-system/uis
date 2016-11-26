package at.ac.tuwien.inso.method_security_tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.service.SemesterService;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SemesterServiceSecurityTests {

    @Autowired
    private SemesterService semesterService;

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void createNotAuthenticated() {
        semesterService.create(new Semester("WS2016"));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void createAuthenticatedAsStudent() {
        semesterService.create(new Semester("WS2016"));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void createAuthenticatedAsLecturer() {
        semesterService.create(new Semester("WS2016"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createAuthenticatedAsAdmin() {
        Semester result = semesterService.create(new Semester("WS2016"));
        assertEquals(result.getLabel(), "WS2016");
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void getCurrentSemesterNotAuthenticated() {
        semesterService.getCurrentSemester();
    }

    @Test
    @WithMockUser
    public void getCurrentSemesterAuthenticated() {
        Semester result = semesterService.getCurrentSemester();
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findAllNotAuthenticated() {
        semesterService.findAll();
    }

    @Test
    @WithMockUser
    public void findAllAuthenticated() {
        List<Semester> result = semesterService.findAll();
        assertNotNull(result);
    }
}
