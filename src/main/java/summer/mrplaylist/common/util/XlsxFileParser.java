package summer.mrplaylist.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.service.PlaylistService;

@Profile({"test", "init"})
@Component
@RequiredArgsConstructor
@Slf4j
public class XlsxFileParser {

	private final PlaylistService playlistService;
	ClassPathResource resource = new ClassPathResource("PlaylistData.xlsx");

	@PostConstruct
	@Transactional
	public void init() throws IOException {
		log.info("PlaylistData.xlsx init");
		fileRead();
	}

	public void fileRead() throws IOException {
		FileInputStream excelFile = new FileInputStream(resource.getFile());
		XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
		XSSFSheet sheet = workbook.getSheetAt(0);

		List<Integer> plIndexList = new ArrayList<>();
		Map<String, List<ArtistForm>> plGroupList = new HashMap<>();

		// 플레이리스트 인덱싱
		for (int rowIdx = 3; rowIdx <= sheet.getPhysicalNumberOfRows(); rowIdx++) {
			XSSFRow plRow = sheet.getRow(rowIdx);
			if (plRow != null) {
				XSSFCell cell = plRow.getCell(0);
				String plName = cell.toString();
				if (!plName.isBlank())
					plIndexList.add(rowIdx);
			}
		}
		plIndexList.add(sheet.getPhysicalNumberOfRows());

		// 파싱
		for (int i = 0; i < plIndexList.size(); i++) {
			XSSFRow plRow = sheet.getRow(plIndexList.get(i));
			if (plRow == null)
				continue;
			// 플레이리스트
			String plName = readCell(plRow, 0);
			String plDescription = readCell(plRow, 1);
			List<String> plCategory = Arrays.asList(readCell(plRow, 2).split(","));

			List<MusicForm> musicFormList = new ArrayList<>();
			for (int rowIdx = plIndexList.get(i); rowIdx < plIndexList.get(i + 1); rowIdx++) {
				XSSFRow row = sheet.getRow(rowIdx);
				if (row == null)
					continue;
				if (rowIdx >= row.getPhysicalNumberOfCells())
					break;
				// 음악
				String musicName = readCell(row, 3);
				if (musicName.isBlank()) {
					break;
				}
				String musicDescription = readCell(row, 4);
				String musicUrl = readCell(row, 5);

				// 그룹
				String groupName = readCell(row, 6);
				String groupDescription = readCell(row, 7);

				// 가수 목록
				List<ArtistForm> artistList = new ArrayList<>();
				for (int colIdx = 9; colIdx <= row.getPhysicalNumberOfCells(); colIdx += 2) {
					String artistName = readCell(row, colIdx);
					String artistDescription = readCell(row, colIdx + 1);
					if (!artistName.isBlank()) {
						artistList.add(new ArtistForm(artistName, artistDescription));
					}
				}

				if (!groupName.isBlank()) {
					MusicForm musicForm = MusicForm.builder()
						.name(musicName)
						.description(musicDescription)
						.url(musicUrl)
						.artistFormList(artistList)
						.groupForm(new GroupForm(groupName, groupDescription))
						.build();

					musicFormList.add(musicForm);
					//                    plGroupList.put(groupName, artistList);
				} else { // 그룹 아닐때
					MusicForm musicForm = MusicForm.builder()
						.name(musicName)
						.description(musicDescription)
						.url(musicUrl)
						.artistFormList(artistList)
						.groupForm(null)
						.build();

					musicFormList.add(musicForm);
				}
			}
			PlaylistForm playlistForm = PlaylistForm.builder()
				.plName(plName)
				.plDescription(plDescription)
				.categoryNameList(plCategory)
				.musicFormList(musicFormList)
				.build();
			playlistService.create(playlistForm);
		}

	}

	public String readCell(XSSFRow row, int cellNum) {
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null)
			return "";
		String value = "";
		switch (cell.getCellType()) {
			case STRING: // 텍스트
				value = cell.getStringCellValue();
				break;
			case NUMERIC: // 숫자
				value = cell.getNumericCellValue() + "";
				break;
			case BLANK: // 빈칸
				value = cell.getBooleanCellValue() + "";
				break;
		}
		if (value == "false")
			return "";

		return value;
	}

}


