package summer.mrplaylist.member.model;


import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "password", nullable = false, length = 60) // bcrypt 인코딩시 리턴되는 문자열 길이는 60
    private String password;
    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;
    @Column(name = "profile_img", length = 512)
    private String profileImg;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
