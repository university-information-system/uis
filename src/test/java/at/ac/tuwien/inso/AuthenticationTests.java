package at.ac.tuwien.inso;

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
                redirectedUrl("/login?error")
        );
    }

    @Test
    public void itAuthenticatesAdmin() throws Exception {
        userAccountRepository.save(new UserAccount("admin", "pass", new Role("ROLE_ADMIN")));

        mockMvc.perform(
                formLogin().user("admin").password("pass")
        ).andExpect(
                redirectedUrl("/admin/studyplans")
        );
    }

    @Test
    public void itAuthenticatesLecturer() throws Exception {
        userAccountRepository.save(new UserAccount("lecturer", "pass", new Role("ROLE_LECTURER")));

        mockMvc.perform(
                formLogin().user("lecturer").password("pass")
        ).andExpect(
                redirectedUrl("/lecturer/courses")
        );
    }

    @Test
    public void itAuthenticatesStudent() throws Exception {
        userAccountRepository.save(new UserAccount("student", "pass", new Role("ROLE_STUDENT")));

        mockMvc.perform(
                formLogin().user("student").password("pass")
        ).andExpect(
                redirectedUrl("/student/courses")
        );
    }

    @Test
    public void onLogoutItRedirectsToLoginPage() throws Exception {
        mockMvc.perform(
                logout()
        ).andExpect(
                redirectedUrl("/login")
        );
    }

    @Test
    public void onUnauthorizedAccessItRedirectsToLoginPage() throws Exception {
        mockMvc.perform(
                get("/admin").with(user("student"))
        ).andExpect(
                redirectedUrl("/login")
        );
    }
}
