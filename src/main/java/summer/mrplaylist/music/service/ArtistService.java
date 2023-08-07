package summer.mrplaylist.music.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.model.Group;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.music.model.SoloArtist;
import summer.mrplaylist.music.repository.MainArtistRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistService {

	private final MainArtistRepository mainArtistRepository;

	@Transactional
	public Group createGroupArtist(GroupForm groupForm, List<ArtistForm> artistFormList) {
		Group group = createGroup(groupForm);

		List<SoloArtist> soloArtists = artistFormList.stream()
			.map(artistForm -> createArtist(artistForm))
			.toList();
		log.info("{}", soloArtists);
		for (SoloArtist soloArtist : soloArtists) {
			group.addArtist(soloArtist);
		}

		Group savedGroup = mainArtistRepository.save(group);
		return savedGroup;

	}

	/**
	 * 폼의 처음은 솔로가수거나 멤버이다. 해당을 만드는 메소드
	 * @param artistForm,soloArtist or group
	 * @return
	 */
	@Transactional
	public SoloArtist createArtist(ArtistForm artistForm) {
		Optional<MainArtist> findArtist = mainArtistRepository.findSoloArtistByName(artistForm.getName());
		if (findArtist.isPresent()) {
			SoloArtist artist = (SoloArtist)findArtist.get();
			artist.addDescription(artistForm.getDescription());
			return artist;
		} else {
			SoloArtist artist = SoloArtist.createArtist(artistForm);
			SoloArtist savedArtist = mainArtistRepository.save(artist);
			return savedArtist;
		}
	}

	// 위와 비슷하지만 가수하고 입력값이 달라질것을 생각하여 분리
	@Transactional
	public Group createGroup(GroupForm groupForm) {
		Optional<MainArtist> findGroup = mainArtistRepository.findGroupByName(groupForm.getGroupName());
		if (findGroup.isPresent()) {
			Group group = (Group)findGroup.get();
			group.addDescription(groupForm.getGroupDescription());
			return group;
		} else {
			Group group = Group.createGroup(groupForm);
			Group savedGroup = mainArtistRepository.save(group);
			return savedGroup;
		}
	}
}
