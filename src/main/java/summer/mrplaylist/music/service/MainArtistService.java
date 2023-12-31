package summer.mrplaylist.music.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.constant.ArtistConstants;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.ArtistUpdateForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.model.Group;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.music.model.SoloArtist;
import summer.mrplaylist.music.repository.MainArtistRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MainArtistService {

	private final MainArtistRepository mainArtistRepository;

	@Transactional
	public Group createGroupArtist(GroupForm groupForm, List<ArtistForm> artistFormList) {
		Group group = createGroup(groupForm);

		List<SoloArtist> soloArtists = artistFormList.stream()
			.map(artistForm -> createArtist(artistForm))
			.toList();
		log.info("SoloArtist = {}", soloArtists);

		soloArtists = soloArtists.stream()
			.filter(artist -> !group.getGroupSoloArtistList().contains(artist))
			.collect(Collectors.toList());
		log.info("Not duplicate artist = {}", soloArtists);

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

	@Transactional
	// 이름 설명 수정 (그룹, 가수)
	public MainArtist update(ArtistUpdateForm artistUpdateForm) {
		MainArtist mainArtist = findMainArtist(artistUpdateForm.getId());
		mainArtist.update(artistUpdateForm);
		return mainArtist;
	}

	/**
	 * 솔로가수 삭제
	 */
	@Transactional
	public void deleteArtist(Long artistId) {
		MainArtist artist = mainArtistRepository.findById(artistId)
			.orElseThrow(() -> new IllegalStateException(ArtistConstants.NOT_FOUND));
		if (artist instanceof SoloArtist soloArtist) {
			soloArtist.deleteGroup();
		}
		mainArtistRepository.delete(artist);
	}

	public MainArtist findMainArtist(Long id) {
		MainArtist mainArtist = mainArtistRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(ArtistConstants.NOT_FOUND));
		return mainArtist;
	}
}
