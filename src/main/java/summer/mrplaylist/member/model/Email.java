package summer.mrplaylist.member.model;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Email {
    private String toEmail;

    private String subject;

    private String context;

    public static Email createEmail(String toEmail, String subject, String context) {
        Email email = Email.builder()
                .toEmail(toEmail)
                .subject(subject)
                .context(context)
                .build();

        return email;
    }
}
