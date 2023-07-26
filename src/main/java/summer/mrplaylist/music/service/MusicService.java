package summer.mrplaylist.music.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Artist;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.repository.MusicRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final ArtistService artistService;
    @Transactional
    public Music create(MusicForm musicForm, List<ArtistForm> artistFormList){
        Artist mainArtist = artistService.createArtist(artistFormList);
        Music music = Music.createMusic(musicForm, mainArtist);
        return musicRepository.save(music);
    }

}
