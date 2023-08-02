package summer.mrplaylist.playlist.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.model.Playlist;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static summer.mrplaylist.CreateMethod.*;
import static summer.mrplaylist.CreateMethod.getMember;

@Slf4j
@Transactional
@SpringBootTest
@DisplayName("플레이 리스트 생성 통합 테스트")
public class PlaylistServiceTest {

    @Autowired
    PlaylistService playlistService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("플레이리스트 생성")
    public void createPlaylist() throws Exception {
        //given
        Member member = getMember();
        member = memberRepository.save(member);

        List<String> categoryNameList = getCategoryNameList();

        PlaylistForm playlistForm = PlaylistForm.builder()
                .plName("킹 누의 명곡")
                .plDescription("노동요")
                .categoryNameList(categoryNameList)
                .member(member)
                .build();

        List<MusicForm> musicFormList = getMusicFormList();
        //when

        Playlist playlist = playlistService.create(playlistForm, musicFormList);
        //then

        assertThat(playlist.getName()).isEqualTo(playlistForm.getPlName());
        assertThat(playlist.getMusicCount()).isEqualTo(1);
        assertThat(playlist.getMember()).isEqualTo(member);
        assertThat(playlist.getMusicList().stream().map(Music::getName)).contains("역몽");

        log.info("플레이리스트에 속한 대표 가수: {}",playlist.getMusicList().stream().map((m -> m.getArtist().getName())).collect(Collectors.toList()));
    }
}