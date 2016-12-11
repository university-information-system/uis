package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void itRedirectsToLoginErrorOnAuthenticationError() throws Exception {
        mockMvc.perform(
                formLogin().user("unknown").password("pass")
        ).andExpect(
                unauthenticated()
        ).andExpect(
                redirectedUrl("/login?error")
        );
    }

    @Test
    public void itAuthenticatesAdmin() throws Exception {
        userAccountRepository.save(new UserAccount("admin", "pass", Role.ADMIN));

        mockMvc.perform(
                formLogin().user("admin").password("pass")
        ).andExpect(
                authenticated().withRoles(Role.ADMIN.name())
        ).andExpect(
                redirectedUrl("/")
        );
    }

    @Test
    public void itAuthenticatesLecturer() throws Exception {
        userAccountRepository.save(new UserAccount("lecturer", "pass", Role.LECTURER));

        mockMvc.perform(
                formLogin().user("lecturer").password("pass")
        ).andExpect(
                authenticated().withRoles(Role.LECTURER.name())
        ).andExpect(
                redirectedUrl("/")
        );
    }

    @Test
    public void itAuthenticatesStudent() throws Exception {
        userAccountRepository.save(new UserAccount("student", "pass", Role.STUDENT));

        mockMvc.perform(
                formLogin().user("student").password("pass")
        ).andExpect(
                authenticated().withRoles(Role.STUDENT.name())
        ).andExpect(
                redirectedUrl("/")
        );
    }

    @Test
    public void onLogoutUserIsNoLongerAuthenticatedAndRedirectedToLoginPage() throws Exception {
        mockMvc.perform(
                logout()
        ).andExpect(
                unauthenticated()
        ).andExpect(
                redirectedUrl("/login")
        );
    }

    @Test
    public void onUnauthorizedAccessItReturnsAccessDenied() throws Exception {
        mockMvc.perform(
                get("/admin").with(user("student"))
        ).andExpect(
                status().isForbidden()
        );
    }
}
