package at.ac.tuwien.inso.integration_tests;

import static at.ac.tuwien.inso.controller.Constants.MAX_PAGE_SIZE;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.repository.UisUserRepository;

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
            listUsers(2)
        ).andExpect(
            resultHasUsersPage(
                page(
                    2,
                    MAX_PAGE_SIZE,
                    users.size(),
                    users
                        .stream()
                        .skip(2 * MAX_PAGE_SIZE)
                        .limit(MAX_PAGE_SIZE)
                        .collect(Collectors.toList())
                )
            )
        );
    }

    private MockHttpServletRequestBuilder listUsers(int page) {
        return get("/admin/users/page/" + (page + 1))
                .with(user("admin").roles(Role.ADMIN.name()));
    }

    @Test
    public void onListAllItLimitsPageSize() throws Exception {
        mockMvc.perform(
            listUsers()
        ).andExpect(
            resultHasUsersPage(
                page(
                    0,
                    MAX_PAGE_SIZE,
                    users.size(),
                    users
                        .stream()
                        .limit(MAX_PAGE_SIZE)
                        .collect(Collectors.toList())
                )
            )
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

    @Test
    public void adminListUsersForPageSearchNullAndPageNumberOneTest() throws Exception {

        mockMvc.perform(
                get("/admin/users/page/1")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .param("pageNumber", "1")
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/admin/users")
        );

    }

    @Test
    public void adminListUsersForPageSearchEmptyTest() throws Exception {

        mockMvc.perform(
                get("/admin/users/page/1")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .param("search", "")
                        .param("pageNumber", "1")
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/admin/users/page/1")
        );

    }

    @Test
    public void adminListUsersForPagePageNumberOneTest() throws Exception {

        mockMvc.perform(
                get("/admin/users/page/1")
                        .with(user("admin").roles(Role.ADMIN.name()))
                        .param("search", "something")
                        .param("pageNumber", "1")
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/admin/users?search=something")
        );

    }

    @Test
    public void adminShouldSeeStudentDetails() {
        //TODO
    }

    @Test
    public void adminShouldSeeLecturerDetails() {
        //TODO
    }
}
