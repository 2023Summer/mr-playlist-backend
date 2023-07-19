package summer.mrplaylist.member.model;


import jakarta.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "profile_img")
    private String profileImg;
}
