package summer.mrplaylist.music.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.model.Artist;
import summer.mrplaylist.music.repository.ArtistRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Transactional
    public Artist createArtist(List<ArtistForm> artistFormList){
        if (artistFormList.size() == 1){
            ArtistForm artistForm = artistFormList.get(0);
            return createArtistByForm(artistForm);
        }
        else{
            return createGroupArtist(artistFormList);
        }
    }

    /**
     * 첫번째 그룹과 하위 멤버들을 만들어주는 메소드
     * @param artistFormList
     * @return
     */
    @Transactional
    public Artist createGroupArtist(List<ArtistForm> artistFormList) {
        Artist group = Artist.createArtist(artistFormList.get(0));

        List<Artist> artistList = artistFormList.stream().skip(1)
                .map(artistForm -> createArtistByForm(artistForm))
                .toList();
        log.info("{}",artistList);
        for (Artist artist : artistList) {
            group.addArtist(artist);
        }

        Artist savedGroup = artistRepository.save(group);
        return savedGroup;

    }
    /**
     * 폼의 처음은 솔로가수거나 멤버이다. 해당을 만드는 메소드
     * @param artistForm,soloArtist or group
     * @return
     */
    @Transactional
    public Artist createArtistByForm(ArtistForm artistForm) {
        Optional<Artist> findArtist = artistRepository.findArtistByName(artistForm.getName());
        if (findArtist.isPresent()){
            Artist artist = findArtist.get();
            artist.addDescription(artistForm.getDescription());
            return artist;
        }else{
            Artist artist = Artist.createArtist(artistForm);
            Artist savedArtist = artistRepository.save(artist);
            return savedArtist;
        }
    }
}
