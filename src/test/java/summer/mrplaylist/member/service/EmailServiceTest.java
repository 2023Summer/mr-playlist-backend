package summer.mrplaylist.member.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import summer.mrplaylist.member.model.Email;


import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class EmailServiceTest {
    private static final String FROM = "admin@gmail.com";
    private static final String TO = "user@gmail.com";
    private static final String SUBJECT = "subject";
    private static final String CONTEXT = "Context";

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);
    @Autowired
    private EmailService emailService;

    @Test
    public void sendEmail() throws Exception {
        emailService.sendEmail(Email.builder()
                .toEmail(TO)
                .subject(SUBJECT)
                .context(CONTEXT).build());

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages.length).isEqualTo(1);

        // Check Message
        MimeMessage receivedMessage = receivedMessages[0];
        assertThat(receivedMessage.getFrom()[0].toString()).isEqualTo(FROM);
        assertThat(receivedMessage.getSubject()).isEqualTo(SUBJECT);
        assertThat(receivedMessage.getContent().toString()).contains(CONTEXT);
        // Or
        assertThat(CONTEXT).isEqualTo(GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
    }


}