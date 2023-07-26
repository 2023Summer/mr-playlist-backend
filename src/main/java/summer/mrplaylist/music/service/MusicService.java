package summer.mrplaylist.music.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.music.constant.ArtistConstants;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Artist;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.repository.ArtistRepository;
import summer.mrplaylist.music.repository.MusicRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final ArtistRepository artistRepository;
    @Transactional
    public Music create(MusicForm musicForm, Long artistId){
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalStateException(ArtistConstants.NOT_FOUND_ARTIST));
        Music music = Music.createMusic(musicForm, artist);
        return musicRepository.save(music);
    }

}
