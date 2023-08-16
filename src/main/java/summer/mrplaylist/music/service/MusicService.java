package summer.mrplaylist.music.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.constant.MusicConstants;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.dto.MusicUpdateForm;
import summer.mrplaylist.music.model.Group;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.model.SoloArtist;
import summer.mrplaylist.music.repository.MusicRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MusicService {

	private final MusicRepository musicRepository;
	private final MainArtistService mainArtistService;

	@Transactional

	public Music create(MusicForm musicForm) {
		if (musicForm.getGroupForm() != null) {
			Group Group = mainArtistService.createGroupArtist(musicForm.getGroupForm(), musicForm.getArtistFormList());
			Music music = Music.createMusic(musicForm, Group);
			return musicRepository.save(music);
		} else {
			ArtistForm artistForm = musicForm.getArtistFormList().get(0);

			SoloArtist artist = mainArtistService.createArtist(artistForm);
			Music music = Music.createMusic(musicForm, artist);
			return musicRepository.save(music);
		}
	}

	// 새로운 그룹을 만들시 혹은 가수를 만들시는 새 팝업
	@Transactional
	public Music update(MusicUpdateForm updateForm) {
		Music music = findMusic(updateForm.getMusicId());

		MainArtist mainArtist = mainArtistService.findMainArtist(updateForm.getMainArtistId());
		music.updateInfo(updateForm, mainArtist);

		return music;
	}

	@Transactional
	public void delete(Long musicId) {
		Music music = findMusic(musicId);
		music.getPlaylist().deleteMusic(music);
		musicRepository.delete(music);
	}

	public Music findMusic(Long musicId) {
		Music music = musicRepository.findById(musicId)
			.orElseThrow(() -> new IllegalStateException(MusicConstants.NOT_FOUND));
		return music;

	}

}
