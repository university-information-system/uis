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

import static java.util.Arrays.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminUsersTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void itListsAllUsers() throws Exception {
        Role role = roleRepository.save(new Role("ROLE_ADMIN"));
        Iterable<UserAccount> users =  userAccountRepository.save(asList(
                new UserAccount("admin 1", "pass", role),
                new UserAccount("admin 2", "pass", role)
        ));

        mockMvc.perform(
                post("/admin/users").with(user("admin").roles("ADMIN"))
        ).andExpect(
                model().attribute("usersAttr", users)
        );
    }
}
