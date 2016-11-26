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

import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.service.UserCreationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserCreationServiceSecurityTests {

    @Autowired
    private UserCreationService userCreationService;

    private UisUser user = new Student("student1", "student", "student@student.com");


    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void createNotAuthenticated() {
        userCreationService.create(user);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void createAuthenticatedAsStudent() {
        userCreationService.create(user);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void createAuthenticatedAsLecturer() {
        userCreationService.create(user);
    }

    @Test(expected = NullPointerException.class)
    @WithMockUser(roles = "ADMIN")
    public void createAuthenticatedAsAdmin() {
        userCreationService.create(user);
    }

}
