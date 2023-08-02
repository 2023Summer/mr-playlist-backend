package summer.mrplaylist.music.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final ArtistService artistService;
    @Transactional
    public Music create(MusicForm musicForm){
        if (musicForm.getGroupForm() != null) {
            Group Group = artistService.createGroupArtist(musicForm.getGroupForm(), musicForm.getArtistFormList());
            Music music = Music.createMusic(musicForm, Group);
            return musicRepository.save(music);
        }

        else{
            ArtistForm artistForm = musicForm.getArtistFormList().get(0);

            SoloArtist artist = artistService.createArtist(artistForm);
            Music music = Music.createMusic(musicForm, artist);
            return musicRepository.save(music);
        }

    }

}
