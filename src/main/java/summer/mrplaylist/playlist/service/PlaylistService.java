package summer.mrplaylist.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.service.MusicService;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.model.PlaylistCategory;
import summer.mrplaylist.playlist.repository.PlaylistRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaylistService {

    private final MusicService musicService;
    private final PlaylistRepository plRepository;
    private final PlaylistCategoryService plcService;

    @Transactional
    public Playlist create(PlaylistForm playlistForm, List<MusicForm> musicFormList){

        Playlist playlist = Playlist.createPlaylist(playlistForm);
        musicFormList.stream().map(musicService::create).forEach(music -> music.addPlaylist(playlist));
        plcService.join(playlist,playlistForm.getCategoryNameList());
        return plRepository.save(playlist);

    }


}
