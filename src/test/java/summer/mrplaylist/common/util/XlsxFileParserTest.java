package summer.mrplaylist.common.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.repository.PlaylistRepository;

@SpringBootTest
@Transactional
public class XlsxFileParserTest {
	@Autowired
	private XlsxFileParser xlsxFileParser;

	@Autowired
	private PlaylistRepository playlistRepository;

	@Test
	void fileRead() throws Exception {

		xlsxFileParser.fileRead();
		Playlist playlist = playlistRepository.findById(1L)
			.orElseThrow(() -> new IllegalArgumentException("not exist playlist"));

		Assertions.assertThat(playlist.getName()).isEqualTo("킹누의 노래 모음");
	}
}
