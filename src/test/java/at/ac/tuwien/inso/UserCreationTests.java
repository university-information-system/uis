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
import org.springframework.mail.javamail.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
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
    private JavaMailSender mockMailSender;

    @Value("${uis.server.account.activation.url.prefix}")
    private String accountActivationUrlPrefix;

    @Autowired
    private Messages messages;

    private CreateUserForm form;

    private MimeMessage msg;

    @Before
    public void setUp() throws Exception {
        Mockito.reset(mockMailSender);
        Mockito.when(mockMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
    }

    @Test
    public void onCreateStudentAccountItSendsActivationEmailAndStoresPendingAccountActivation() throws Exception {
        checkUserCreation(new CreateUserForm(CreateUserForm.STUDENT, "Student", "student@uis.at"));

    }

    private void checkUserCreation(CreateUserForm form) throws Exception {
        createUser(form)
                .andExpect(redirectedUrl("/admin/users"))
                .andExpect(this::flashMessageSet)
                .andExpect(this::activationMailSent)
                .andExpect(this::activationEntityCreated);
    }

    private ResultActions createUser(CreateUserForm form) throws Exception {
        this.form = form;

        return mockMvc.perform(
                post("/admin/users/create").with(user("admin").roles(Role.ADMIN.name()))
                        .param("type", form.getType())
                        .param("name", form.getName())
                        .param("email", form.getEmail())
                        .with(csrf())
        );
    }

    private void flashMessageSet(MvcResult result) {
        String msgKey = (String) result.getFlashMap().get("flashMessage");

        assertEquals("admin.users.create.success", msgKey);
    }

    private void activationMailSent(MvcResult result) throws MessagingException {
        msg = getSentMail();

        assertEquals(form.getEmail(), msg.getAllRecipients()[0].toString());
        assertEquals(messages.get(UserCreationService.MAIL_SUBJECT), msg.getSubject());
    }

    private MimeMessage getSentMail() {
        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        Mockito.verify(mockMailSender).send(captor.capture());
        return captor.getValue();
    }

    private void activationEntityCreated(MvcResult result) throws IOException, MessagingException {
        String activationId = getActivationIdFromMail();

        PendingAccountActivation activation = pendingAccountActivationRepository.findOne(activationId);
        assertNotNull(activation);

        assertEquals(form.getName(), activation.getForUser().getName());
    }

    private String getActivationIdFromMail() throws IOException, MessagingException {
        Matcher activationLinkMatcher = Pattern.compile(accountActivationUrlPrefix + UUID_PATTERN).matcher(msg.getContent().toString());
        assertTrue(activationLinkMatcher.find());

        return activationLinkMatcher.group(1);
    }

    @Test
    public void onCreateLecturerItSendsActivationEmailAndStoresPendingAccountActivation() throws Exception {
        checkUserCreation(new CreateUserForm(CreateUserForm.LECTURER, "Lecturer", "lecturer@uis.at"));
    }

    @Test
    public void onInvalidUserItSetsAttributeErrors() throws Exception {
        createUser(new CreateUserForm("unknown", "", "invalid"))
                .andExpect(model().attributeHasFieldErrors("createUserForm", "type", "name", "email"));
    }
}
