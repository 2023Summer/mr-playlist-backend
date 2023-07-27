package summer.mrplaylist.member.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.common.service.RedisService;
import summer.mrplaylist.member.model.Email;
import summer.mrplaylist.member.repository.MemberRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {
    private final RedisService redisService;
    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;

    public void sendEmail(Email email){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, false, "UTF-8");
            mimeMessageHelper.setTo(email.getToEmail());
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setText(email.getContext());

            javaMailSender.send(message);
            log.info("메일 전송 성공 : {} {}", email.getToEmail(), email.getSubject());
        }
        catch (MessagingException e) {
            log.error("메일 전송 오류 : {}", e.getMessage());
        }
    }

    public void sendAuthEmail(String toEmail) {
        if (memberRepository.existsByEmail(toEmail)){
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
        String authNumber = createAuthNumber(toEmail);
        String subject = "회원 가입 인증메일";
        String context = "인증번호 : " + authNumber;
        Email email = Email.createEmail(toEmail, subject, context);
        sendEmail(email);
    }


    private String createAuthNumber(String email) {
        String authNumber = UUID.randomUUID().toString().substring(0,6); // 6자리 랜덤 인증번호 생성
        redisService.setDataWithExpire(email, authNumber, 300L); // 유효시간 5분
        return authNumber;
    }

    public boolean checkAuthNumber(String email, String userAuthNumber) {
        String authNumber = redisService.getData(email);
        if(userAuthNumber.equals(authNumber)) {
            return true;
        }
        else {
            return false;
        }
    }

}
