package summer.mrplaylist.music.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    @Column(name = "url", length = 512, nullable = false)
    private String url;
    @Column(name = "name", length = 200)
    private String description;
    @Column(name = "views", nullable = false)
    @ColumnDefault("0")
    private Integer views;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
