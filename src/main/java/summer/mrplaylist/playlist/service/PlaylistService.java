package summer.mrplaylist.playlist.service;

import static summer.mrplaylist.playlist.constant.PlaylistConstant.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.service.MusicService;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.repository.PlaylistRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaylistService {

	private final MusicService musicService;
	private final PlaylistRepository plRepository;
	private final PlaylistCategoryService plcService;

	@Transactional
	public Playlist create(PlaylistForm playlistForm, List<MusicForm> musicFormList) {
		Playlist playlist = Playlist.createPlaylist(playlistForm);
		Playlist savedPlayList = plRepository.save(playlist);
		plcService.join(savedPlayList, playlistForm.getCategoryNameList());
		return addMusic(savedPlayList.getId(), musicFormList);

	}

	@Transactional
	public Playlist addMusic(Long playlistId, List<MusicForm> musicFormList) {

		Playlist playlist = findPlaylist(playlistId);
		musicFormList.stream().map(musicService::create).forEach(music -> music.addPlaylist(playlist));
		return playlist;

	}

	public Playlist findPlaylist(Long playlistId) {
		return plRepository.findById(playlistId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PLAYLIST));
	}

}
