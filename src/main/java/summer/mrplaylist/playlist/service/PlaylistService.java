package summer.mrplaylist.playlist.service;

import static summer.mrplaylist.playlist.constant.PlaylistConstant.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.repository.MusicQRepo;
import summer.mrplaylist.music.service.MusicService;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.dto.PlaylistResponse;
import summer.mrplaylist.playlist.dto.PlaylistSimpleResponse;
import summer.mrplaylist.playlist.dto.PlaylistUpdateForm;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.repository.PlaylistQRepo;
import summer.mrplaylist.playlist.repository.PlaylistRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaylistService {

	private final MusicService musicService;
	private final PlaylistRepository plRepository;
	private final PlaylistQRepo playlistQRepo;
	private final MusicQRepo musicQRepo;
	private final PlaylistCategoryService plcService;

	@Transactional
	public Playlist create(PlaylistForm playlistForm) {
		Playlist playlist = Playlist.createPlaylist(playlistForm);
		Playlist savedPlayList = plRepository.save(playlist);
		plcService.join(savedPlayList, playlistForm.getCategoryNameList());
		return addMusic(savedPlayList.getId(), playlistForm.getMusicFormList());
	}

	@Transactional
	public Playlist updateInfo(PlaylistUpdateForm updateForm) {
		Playlist playlist = findPlaylist(updateForm.getPlId());
		playlist.updateInfo(updateForm);
		return playlist;
	}

	@Transactional
	public void delete(Long playListId) {
		Playlist playlist = findPlaylist(playListId);
		plRepository.delete(playlist);
	}

	@Transactional
	public Playlist addMusic(Long playlistId, List<MusicForm> musicFormList) {
		Playlist playlist = findPlaylist(playlistId);
		musicFormList.stream().forEach(musicForm -> musicService.createWithPlaylist(musicForm, playlist));
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

	public PlaylistResponse findPlaylistInfo(Long playlistId) {
		Playlist playlist = findPlaylist(playlistId);
		List<Music> musicList = musicQRepo.findMusicWithArtist(playlist);
		return new PlaylistResponse(playlist, musicList);
	}

	public List<PlaylistSimpleResponse> findPlaylistOrderByCond(String cond, Pageable pageable) {
		List<Playlist> playlists = playlistQRepo.orderByCond(cond, pageable);
		List<PlaylistSimpleResponse> playlistSimpleResponses = getPlaylistSimpleResponses(playlists);
		return playlistSimpleResponses;

	}

	public Playlist findPlaylist(Long playlistId) {
		return plRepository.findById(playlistId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PLAYLIST));
	}

	public List<PlaylistSimpleResponse> getPlaylistByCategory(String categoryName, Pageable pageable) {
		Page<Playlist> playlists = playlistQRepo.findHavingCategory(categoryName, pageable);
		return getPlaylistSimpleResponses(playlists.getContent());
	}

	private static List<PlaylistSimpleResponse> getPlaylistSimpleResponses(List<Playlist> playlists) {
		List<PlaylistSimpleResponse> playlistSimpleResponses = playlists.stream()
			.map(PlaylistSimpleResponse::new)
			.toList();
		return playlistSimpleResponses;
	}

}
