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
import org.springframework.test.web.servlet.request.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

import static java.util.Arrays.*;
import static org.hamcrest.core.IsEqual.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AdminUsersTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UisUserRepository uisUserRepository;

    private List<UisUser> users;

    @Before
    public void setUp() throws Exception {
        uisUserRepository.save(asList(
                new Student("s1234567", "student", "mail@uis.at"),
                new Student("s1234568", "John", "student@uis.at"),
                new Student("s1234569", "Johny", "student@uis.at"),
                new Lecturer("l1234567", "lecturer", "lecturer@uis.at")
        ));

        this.users = uisUserRepository.findAllMatching("");
    }

    @Test
    public void itListsAllUsers() throws Exception {
        mockMvc.perform(
                listUsers()
        ).andExpect(
                resultHasUsers(users)
        );
    }

    private ResultMatcher resultHasUsers(List<UisUser> users) {
        return model().attribute("users", equalTo(users));
    }

    private MockHttpServletRequestBuilder listUsers() {
        return get("/admin/users").with(user("admin").roles(Role.ADMIN.name()));
    }

    @Test
    public void itSearchesUsersByIdentificationNumber() throws Exception {
        List<UisUser> usersWithIdNumber = users.stream()
                .filter(it -> it.getIdentificationNumber().equals("l1234567"))
                .collect(Collectors.toList());

        mockMvc.perform(
                listUsersWithSearchFilter("l1234567")
        ).andExpect(
                resultHasUsers(usersWithIdNumber)
        );
    }

    private MockHttpServletRequestBuilder listUsersWithSearchFilter(String filter) {
        return listUsers().param("search", filter);
    }

    @Test
    public void itSearchesUsersByNameIgnoringCase() throws Exception {
        List<UisUser> usersWithNameJohn = users.stream()
                .filter(it -> it.getName().startsWith("John"))
                .collect(Collectors.toList());

        mockMvc.perform(
                listUsersWithSearchFilter("jOhN")
        ).andExpect(
                resultHasUsers(usersWithNameJohn)
        );
    }

    @Test
    public void itSearchesUsersByMailIgnoringCase() throws Exception {
        List<UisUser> usersWithMail = users.stream()
                .filter(it -> it.getEmail().startsWith("mail"))
                .collect(Collectors.toList());

        mockMvc.perform(
                listUsersWithSearchFilter("mAiL")
        ).andExpect(
                resultHasUsers(usersWithMail)
        );
    }

    @Test
    public void itSearchesUsersByCombiningResultsOverMultipleMatchingAttributes() throws Exception {
        List<UisUser> matchingUsers = users.stream()
                .filter(it -> it.getName().startsWith("student") || it.getEmail().startsWith("student"))
                .collect(Collectors.toList());

        mockMvc.perform(
                listUsersWithSearchFilter("StudenT")
        ).andExpect(
                resultHasUsers(matchingUsers)
        );
    }
}
