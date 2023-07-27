package summer.mrplaylist.member.model;


import lombok.Builder;
import lombok.Data;

@Data
public class Email {
    private String toEmail;

    private String subject;

    private String context;

    @Builder
    public static Email createEmail(String toEmail, String subject, String context) {
        return Email.builder()
                .toEmail(toEmail)
                .subject(subject)
                .context(context)
                .build();
    }
}
