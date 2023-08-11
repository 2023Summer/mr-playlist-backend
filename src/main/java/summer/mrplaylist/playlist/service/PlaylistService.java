package summer.mrplaylist.playlist.service;

import static summer.mrplaylist.playlist.constant.PlaylistConstant.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.service.MusicService;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.dto.PlaylistUpdateForm;
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
	public Playlist updateInfo(PlaylistUpdateForm updateForm) {
		Playlist playlist = findPlaylist(updateForm.getPlId());
		playlist.updateInfo(updateForm);
		return playlist;
	}

	// 음악 삭제 연산만 있으면
	// 1:N 이라 충분함

	@Transactional
	public void delete(Long playListId) {
		Playlist playlist = findPlaylist(playListId);
		plRepository.delete(playlist);
	}

	@Transactional
	public Playlist addMusic(Long playlistId, List<MusicForm> musicFormList) {
		Playlist playlist = findPlaylist(playlistId);
		musicFormList.stream().map(musicService::create).forEach(music -> music.addPlaylist(playlist));
		return playlist;
	}

	@Transactional
	public void deleteMusic(Long playListId, Long musicId) {
		Playlist playlist = findPlaylist(playListId);
		if (playlist.getMusicCount() == 1) {
			throw new IllegalStateException("플레이리스트는 촤소 1개의 음악을 가져야 합니다.");
		} else {
			Music music = musicService.findMusic(musicId);
			playlist.deleteMusic(music);
		}
	}

	public Playlist findPlaylist(Long playlistId) {
		return plRepository.findById(playlistId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PLAYLIST));
	}

}
