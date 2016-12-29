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

import java.math.BigDecimal;

import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.SubjectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SubjectServiceSecurityTests {

    @Autowired
    private SubjectService subjectService;


    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void createNotAuthenticated() {
        subjectService.create(new Subject());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void createAuthenticatedAsStudent() {
        subjectService.create(new Subject());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void createAuthenticatedAsLecturer() {
        subjectService.create(new Subject());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createAuthenticatedAsAdmin() {
        subjectService.create(new Subject("ASE", BigDecimal.ONE));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findOneNotAuthenticated() {
        subjectService.findOne(Long.valueOf(1));
    }

    @Test
    @WithMockUser
    public void findOneAuthenticated() {
        subjectService.findOne(Long.valueOf(1));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void searchForSubjectsNotAuthenticated() {
        subjectService.searchForSubjects("ASE");
    }

    @Test
    @WithMockUser
    public void searchForSubjectsAuthenticated() {
        subjectService.searchForSubjects("ASE");
    }

}
