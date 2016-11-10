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

import static org.hamcrest.Matchers.*;
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
    private UisUserRepository uisUserRepository;

    @Test
    public void itListsAllUsers() throws Exception {
        UisUser student = uisUserRepository.save(new Student("student", "email"));
        UisUser lecturer = uisUserRepository.save(new Lecturer("lecturer", "email"));

        mockMvc.perform(
                get("/admin/users").with(user("admin").roles(Role.ADMIN.name()))
        ).andExpect(
                model().attribute("users", hasItems(student, lecturer))
        );
    }
}
