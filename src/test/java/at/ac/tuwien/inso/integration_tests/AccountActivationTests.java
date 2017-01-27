package at.ac.tuwien.inso.integration_tests;

import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.controller.forms.AccountActivationForm;
import at.ac.tuwien.inso.entity.PendingAccountActivation;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.PendingAccountActivationRepository;
import at.ac.tuwien.inso.repository.UserAccountRepository;
import at.ac.tuwien.inso.service.impl.Messages;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AccountActivationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PendingAccountActivationRepository pendingAccountActivationRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private Messages messages;

    private PendingAccountActivation pendingAccountActivation;

    @Before
    public void setUp() throws Exception {
        this.pendingAccountActivation = pendingAccountActivationRepository.save(
                new PendingAccountActivation(new Student("s1234567", "Student", "student@uis.at"))
        );

        userAccountRepository.save(new UserAccount("student", "pass", Role.STUDENT));
    }

    @Test
    public void itReturnsErrorPageOnAccountActivationLinkWithInvalidActivationCode() throws Exception {
        mockMvc.perform(
                getAccountActivationView("unknown")
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                view().name("error")
        ).andExpect(
                model().attribute("message", messages.msg("error.activation_code.notfound"))
        );
    }

    private MockHttpServletRequestBuilder getAccountActivationView(String activationCode) {
        return get("/account_activation/" + activationCode);
    }

    @Test
    public void itReturnsAccountActivationPageOnAccountActivationLinkWithValidActivationCode() throws Exception {
        mockMvc.perform(
                getAccountActivationView(pendingAccountActivation.getId())
        ).andExpect(
                status().isOk()
        ).andExpect(
                view().name("account-activation")
        ).andExpect(
                model().attribute("user", pendingAccountActivation.getForUser())
        );
    }

    @Test
    public void itReturnsErrorPageOnActivateAccountWithInvalidActivationCode() throws Exception {
        mockMvc.perform(
                activateAccount("unknown", new AccountActivationForm("pass", "pass"))
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                view().name("error")
        ).andExpect(
                model().attribute("message", messages.msg("error.activation_code.notfound"))
        );
    }

    private RequestBuilder activateAccount(String activationCode, AccountActivationForm accountActivationForm) {
        return post("/account_activation/" + activationCode)
                .with(csrf())
                .param("password", accountActivationForm.getPassword())
                .param("repeatPassword", accountActivationForm.getRepeatPassword());
    }

    @Test
    public void itDoesNotActivateAccountOnEmptyFormFields() throws Exception {
        mockMvc.perform(
                activateAccount(pendingAccountActivation.getId(), new AccountActivationForm("", ""))
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                model().attribute("user", pendingAccountActivation.getForUser())
        ).andExpect(
                model().attributeHasFieldErrors("accountActivationForm", "password", "repeatPassword")
        );
    }

    @Test
    public void itDoesNotActivateAccountIfRepeatPasswordDoesNotMatch() throws Exception {
        mockMvc.perform(
                activateAccount(pendingAccountActivation.getId(), new AccountActivationForm("pass1", "pass2"))
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                model().attribute("user", pendingAccountActivation.getForUser())
        ).andExpect(r ->
                model().attributeHasErrors("accountActivationForm")
        );
    }

    @Test
    public void itActivatesUserAccount() throws Exception {
        mockMvc.perform(
                activateAccount(pendingAccountActivation.getId(), new AccountActivationForm("pass", "pass"))
        ).andExpect(
                redirectedUrl("/login")
        ).andExpect(
                flash().attribute("flashMessage", "account.activated")
        ).andExpect(
                this::pendingAccountActivationRemoved
        ).andExpect(
                this::userCanLogin
        );
    }

    private void pendingAccountActivationRemoved(MvcResult resultMatcher) {
        assertNull(pendingAccountActivationRepository.findOne(pendingAccountActivation.getId()));
    }

    private void userCanLogin(MvcResult result) throws Exception {
        mockMvc.perform(
                formLogin().user("s1234567").password("pass")
        ).andExpect(
                authenticated().withRoles(Role.STUDENT.name())
        );
    }
}
