package at.ac.tuwien.inso;

import at.ac.tuwien.inso.controller.admin.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.mail.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.*;

import java.util.regex.*;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserCreationTests {

    private static final String UUID_PATTERN = "(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PendingAccountActivationRepository pendingAccountActivationRepository;

    @Autowired
    private MailSender mockMailSender;

    @Value("${uis.server.account.activation.url.prefix}")
    private String accountActivationUrlPrefix;

    @Autowired
    private Messages messages;

    private UisUser user;

    private SimpleMailMessage msg;

    @Before
    public void setUp() throws Exception {
        Mockito.reset(mockMailSender);
    }

    @Test
    public void onCreateStudentAccountItSendsActivationEmailAndStoresPendingAccountActivation() throws Exception {
        checkUserCreation(new CreateUserForm(CreateUserForm.STUDENT, "Student", "student@uis.at"));

    }

    private void checkUserCreation(CreateUserForm form) throws Exception {
        createUser(form)
                .andExpect(status().isCreated())
                .andExpect(view().name("/admin/users"))
                .andDo(this::checkCreatedUser)
                .andDo(this::checkActivationMailSent)
                .andDo(this::checkActivationEntity);
    }

    private ResultActions createUser(CreateUserForm form) throws Exception {
        return mockMvc.perform(
                post("/admin/users").with(user("admin").roles(Role.ADMIN.name()))
                        .param("type", form.getType())
                        .param("name", form.getName())
                        .param("email", form.getEmail())
                        .with(csrf())
        );
    }

    private void checkCreatedUser(MvcResult result) {
        user = (UisUser) result.getModelAndView().getModel().get("createdUser");

        assertNotNull(user);
    }

    private void checkActivationMailSent(MvcResult result) {
        msg = getSentMail();

        assertEquals(user.getEmail(), msg.getTo()[0]);
        assertEquals(messages.get(UserCreationService.MAIL_SUBJECT), msg.getSubject());
    }

    private SimpleMailMessage getSentMail() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(mockMailSender).send(captor.capture());
        return captor.getValue();
    }

    private void checkActivationEntity(MvcResult result) {
        String activationId = getActivationIdFromMail();

        PendingAccountActivation activation = pendingAccountActivationRepository.findOne(activationId);
        assertNotNull(activation);

        assertEquals(user, activation.getForUser());
    }

    private String getActivationIdFromMail() {
        Matcher activationLinkMatcher = Pattern.compile(accountActivationUrlPrefix + UUID_PATTERN).matcher(msg.getText());
        assertTrue(activationLinkMatcher.find());

        return activationLinkMatcher.group(1);
    }

    @Test
    public void onCreateLecturerItSendsActivationEmailAndStoresPendingAccountActivation() throws Exception {
        checkUserCreation(new CreateUserForm(CreateUserForm.LECTURER, "Lecturer", "lecturer@uis.at"));
    }
}
