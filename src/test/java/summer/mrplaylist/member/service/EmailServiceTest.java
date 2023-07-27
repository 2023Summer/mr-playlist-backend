//package summer.mrplaylist.member.service;
//
//import com.icegreen.greenmail.configuration.GreenMailConfiguration;
//import com.icegreen.greenmail.junit5.GreenMailExtension;
//import com.icegreen.greenmail.util.GreenMailUtil;
//import com.icegreen.greenmail.util.ServerSetupTest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.RegisterExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//
//
//import javax.mail.internet.MimeMessage;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class EmailServiceTest {
//
//    @RegisterExtension
//    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
//            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
//            .withPerMethodLifecycle(false);
//
//    @Autowired
//    private EmailService emailService;
//
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    private static final String FROM = "admin@gmail.com";
//    private static final String TO = "user@gmail.com";
//    private static final String SUBJECT = "subject";
//    private static final String CONTEXT = "Context";
//    @Test
//    public void testSendEmail() throws Exception {
//        sendEmail();
//
//        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
//        assertThat(receivedMessages.length).isEqualTo(1);
//
//        // Check Message
//        MimeMessage receivedMessage = receivedMessages[0];
//        assertThat(receivedMessage.getFrom()[0].toString()).isEqualTo(FROM);
//        assertThat(receivedMessage.getSubject()).isEqualTo(SUBJECT);
//        assertThat(receivedMessage.getContent().toString()).contains(CONTEXT);
//        // Or
//        assertThat(CONTEXT).isEqualTo(GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
//    }
//
//    public void sendEmail() {
//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setFrom(FROM);
//        mail.setTo(TO);
//        mail.setSubject(SUBJECT);
//        mail.setText(CONTEXT);
//        javaMailSender.send(mail);
//    }
//
//
//}