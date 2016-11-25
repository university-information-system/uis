package at.ac.tuwien.inso.integration_tests;

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
    private Messages messages;

    private PendingAccountActivation pendingAccountActivation;

    @Before
    public void setUp() throws Exception {
        this.pendingAccountActivation = pendingAccountActivationRepository.save(
                new PendingAccountActivation(new Student("s1234567", "Student", "student@uis.at"))
        );
    }

    @Test
    public void itReturnsErrorPageOnInvalidActivationCode() throws Exception {
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
    public void itReturnsAccountActivationPageOnValidActivationCode() throws Exception {
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
}
