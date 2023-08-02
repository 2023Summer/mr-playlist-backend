package summer.mrplaylist.music.model;

import jakarta.persistence.*;
import lombok.*;
import summer.mrplaylist.music.dto.GroupForm;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@DiscriminatorValue("G")
public class Group extends MainArtist {

    @Column(name = "total_artist")
    private int totalArtist;

    // 그룹 인원
    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST)
    private List<SoloArtist> groupSoloArtistList;

    @Builder
    public Group(Long id, String name, String description, int totalArtist) {
        super(id, name, description,new ArrayList<>());

        this.totalArtist = totalArtist;
        this.groupSoloArtistList = new ArrayList<>();
    }

    public static Group createGroup(GroupForm groupForm){
         return Group.builder()
                .name(groupForm.getGroupName())
                .description(groupForm.getGroupDescription())
                .totalArtist(0)
                .build();
    }

    public void addArtist(SoloArtist soloArtist){
        soloArtist.setGroup(this);
        this.groupSoloArtistList.add(soloArtist);
        this.totalArtist += 1;
    }

}
