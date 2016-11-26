package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.controller.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.impl.*;
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

import static org.junit.Assert.*;
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
                activateAccount("unknown", new AccountActivationForm("user", "pass", "pass"))
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
                .param("username", accountActivationForm.getUsername())
                .param("password", accountActivationForm.getPassword())
                .param("repeatPassword", accountActivationForm.getRepeatPassword());
    }

    @Test
    public void itDoesNotActivateAccountOnEmptyFormFields() throws Exception {
        mockMvc.perform(
                activateAccount(pendingAccountActivation.getId(), new AccountActivationForm("", "", ""))
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                model().attribute("user", pendingAccountActivation.getForUser())
        ).andExpect(
                model().attributeHasFieldErrors("accountActivationForm", "username", "password", "repeatPassword")
        );
    }

    @Test
    public void itDoesNotActivateAccountIfRepeatPasswordDoesNotMatch() throws Exception {
        mockMvc.perform(
                activateAccount(pendingAccountActivation.getId(), new AccountActivationForm("user", "pass1", "pass2"))
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                model().attribute("user", pendingAccountActivation.getForUser())
        ).andExpect(r ->
                model().attributeHasErrors("accountActivationForm")
        );
    }

    @Test
    public void itDoesNotActivateAccountIfUsernameIsNotUnique() throws Exception {
        mockMvc.perform(
                activateAccount(pendingAccountActivation.getId(), new AccountActivationForm("student", "pass", "pass"))
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                model().attribute("user", pendingAccountActivation.getForUser())
        ).andExpect(
                model().attributeHasFieldErrors("accountActivationForm", "username")
        );
    }

    @Test
    public void itActivatesUserAccount() throws Exception {
        mockMvc.perform(
                activateAccount(pendingAccountActivation.getId(), new AccountActivationForm("user", "pass", "pass"))
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
                formLogin().user("user").password("pass")
        ).andExpect(
                authenticated().withRoles(Role.STUDENT.name())
        );
    }
}
