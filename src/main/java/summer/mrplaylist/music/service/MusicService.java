package summer.mrplaylist.music.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Group;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.model.SoloArtist;
import summer.mrplaylist.music.repository.MusicRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final ArtistService artistService;
    @Transactional
    public Music create(MusicForm musicForm, GroupForm groupForm, List<ArtistForm> artistFormList){
        Group Group = artistService.createGroupArtist(groupForm,artistFormList);
        Music music = Music.createMusic(musicForm, Group);
        return musicRepository.save(music);
    }

    @Transactional
    public Music create(MusicForm musicForm,ArtistForm artistForm){
        SoloArtist artist = artistService.createArtist(artistForm);
        Music music = Music.createMusic(musicForm, artist);
        return musicRepository.save(music);
    }


}
