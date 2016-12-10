package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    private List<UisUser> users = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        IntStream.range(0, 100).forEach(it -> {
            UisUser user = it % 2 == 0 ?
                    new Student("s" + it, "student" + it, "smail" + it + "@uis.at") :
                    new Lecturer("l" + it, "lecturer" + it, "lmail" + it + "@uis.at");

            users.add(0, uisUserRepository.save(user));
        });
    }

    @Test
    public void itListsAllUsersWithFirstPageAsDefault() throws Exception {
        mockMvc.perform(
                listUsers()
        ).andExpect(
                resultHasUsersPage(page(0, 10, users.size(), users.stream().limit(10).collect(Collectors.toList())))
        );
    }

    private MockHttpServletRequestBuilder listUsers() {
        return get("/admin/users").with(user("admin").roles(Role.ADMIN.name()));
    }

    private ResultMatcher resultHasUsersPage(Page<UisUser> page) {
        return result -> {
            Page actualPage = (Page) result.getModelAndView().getModel().get("page");

            assertNotNull(actualPage);
            assertThat(actualPage.getNumber(), equalTo(page.getNumber()));
            assertThat(actualPage.getTotalElements(), equalTo(page.getTotalElements()));
            assertThat(actualPage.getSize(), equalTo(page.getSize()));
            assertThat(actualPage.getContent(), equalTo(page.getContent()));
        };
    }

    private <T> Page<T> page(int page, int size, int total, List<T> content) {
        return new PageImpl<>(content, new PageRequest(page, size), total);
    }

    @Test
    public void itListsAllUsersOnExplicitPage() throws Exception {
        mockMvc.perform(
                listUsers(2, 20)
        ).andExpect(
                resultHasUsersPage(page(2, 20, users.size(), users.stream().skip(40).limit(20).collect(Collectors.toList())))
        );
    }

    private MockHttpServletRequestBuilder listUsers(int page, int size) {
        return get("/admin/users")
                .param("page", page + "")
                .param("size", size + "")
                .with(user("admin").roles(Role.ADMIN.name()));
    }

    @Test
    public void onListAllItLimitsPageSize() throws Exception {
        mockMvc.perform(
                listUsers(0, 100)
        ).andExpect(
                resultHasUsersPage(page(0, 50, users.size(), users.stream().limit(50).collect(Collectors.toList())))
        );
    }

    @Test
    public void itSearchesUsersByIdentificationNumber() throws Exception {
        List<UisUser> usersWithIdNumber = users.stream()
                .filter(it -> it.getIdentificationNumber().equals("s0"))
                .collect(Collectors.toList());

        mockMvc.perform(
                listUsersWithSearchFilter("s0")
        ).andExpect(
                resultHasUsersPage(page(0, 10, 1, usersWithIdNumber))
        );
    }

    private MockHttpServletRequestBuilder listUsersWithSearchFilter(String filter) {
        return listUsers().param("search", filter);
    }

    @Test
    public void itSearchesUsersByNameIgnoringCase() throws Exception {
        List<UisUser> usersWithName = users.stream()
                .filter(it -> it.getName().startsWith("student"))
                .collect(Collectors.toList());

        mockMvc.perform(
                listUsersWithSearchFilter("sTudenT")
        ).andExpect(
                resultHasUsersPage(page(0, 10, usersWithName.size(), usersWithName.stream().limit(10).collect(Collectors.toList())))
        );
    }

    @Test
    public void itSearchesUsersByMailIgnoringCase() throws Exception {
        List<UisUser> usersWithMail = users.stream()
                .filter(it -> it.getEmail().startsWith("lmail"))
                .collect(Collectors.toList());

        mockMvc.perform(
                listUsersWithSearchFilter("LmAiL")
        ).andExpect(
                resultHasUsersPage(page(0, 10, usersWithMail.size(), usersWithMail.stream().limit(10).collect(Collectors.toList())))
        );
    }
}
