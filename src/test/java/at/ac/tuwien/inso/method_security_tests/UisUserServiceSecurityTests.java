package at.ac.tuwien.inso.method_security_tests;


import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.service.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.access.*;
import org.springframework.security.authentication.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.transaction.annotation.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UisUserServiceSecurityTests {

    @Autowired
    private UisUserService uisUserService;

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findAllMatchingNotAuthenticated() {
        uisUserService.findAllMatching("", null);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void findAllMatchingAuthenticatedAsStudent() {
        uisUserService.findAllMatching("", null);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void findAllMatchingAuthenticatedAsLecturer() {
        uisUserService.findAllMatching("", null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void findAllMatchingAuthenticatedAsAdmin() {
        uisUserService.findAllMatching("", null);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findOneNotAuthenticated() {
        uisUserService.findOne(Long.valueOf(1));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void findOneAuthenticatedAsStudent() {
        uisUserService.findOne(Long.valueOf(1));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void findOneAuthenticatedAsLecturer() {
        uisUserService.findOne(Long.valueOf(1));
    }

    @Test(expected = BusinessObjectNotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void findOneAuthenticatedAsAdmin() {
        uisUserService.findOne(Long.valueOf(1));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void existsUserWithIdentificationNumberNotAuthenticated() {
        uisUserService.existsUserWithIdentificationNumber("");
    }

    @Test
    @WithMockUser
    public void existsUserWithIdentificationNumberAuthenticated() {
        uisUserService.existsUserWithIdentificationNumber("");
    }

}
