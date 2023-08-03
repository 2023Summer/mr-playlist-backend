package summer.mrplaylist.common.util;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.service.PlaylistService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Transactional
public class XlsxFileParser {

    private final PlaylistService playlistService;
    ClassPathResource resource = new ClassPathResource("PlaylistData.xlsx");
    public void fileRead() throws IOException {
        FileInputStream excelFile = new FileInputStream(resource.getFile());
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        XSSFSheet sheet = workbook.getSheetAt(0);


        // 플레이리스트 인덱싱
        System.out.println("sheet.getPhysicalNumberOfRows() = " + sheet.getPhysicalNumberOfRows());
        List<Integer> plIndexList = new ArrayList<>();
        for (int rowIdx=3; rowIdx<=sheet.getPhysicalNumberOfRows();rowIdx++) {
            XSSFRow plRow = sheet.getRow(rowIdx);
            if(plRow != null) {
                XSSFCell cell = plRow.getCell(0);
                String plName = cell.toString();
                if (!plName.isBlank())
                    plIndexList.add(rowIdx);
            }
        }
        
        for(int plidx : plIndexList){
            System.out.println("plidx = " + plidx);
        }
        // 파싱
        for (int i = 0; i < plIndexList.size();i++) {
            XSSFRow plRow = sheet.getRow(plIndexList.get(i));
            if(plRow == null)
                continue;
            // 플레이리스트
            String plName = readCell(plRow, 0);
            String plDescription = readCell(plRow, 1);
            List<String> plCategory = Arrays.asList(readCell(plRow, 2).split(","));

            PlaylistForm playlistForm = PlaylistForm.builder()
                    .plName(plName)
                    .plDescription(plDescription)
                    .categoryNameList(plCategory)
                    .build();

            List<MusicForm> musicFormList = new ArrayList<>();
            for (int rowIdx=plIndexList.get(i); rowIdx<= plIndexList.get(i+1);rowIdx++) {
                XSSFRow row = sheet.getRow(rowIdx);
                if(row == null)
                    continue;
                if(rowIdx >= row.getPhysicalNumberOfCells())
                    break;
                // 음악
                String musicName = readCell(row, 3);
                String musicDescription = readCell(row, 4);
                String musicUrl = readCell(row, 5);

                // 그룹
                String groupName = readCell(row, 6);
                String groupDescription = readCell(row, 7);

                // 가수 목록
                List<ArtistForm> artistList = new ArrayList<>();
                for (int colIdx=9; colIdx <= row.getPhysicalNumberOfCells(); colIdx+=2) {
                    String artistName = readCell(row, colIdx);
                    String artistDescription = readCell(row, colIdx+1);
                    if (artistName != null) {
                        artistList.add(new ArtistForm(artistName, artistDescription));
                    }
                }
                MusicForm musicForm = MusicForm.builder()
                        .name(musicName)
                        .description(musicDescription)
                        .url(musicUrl)
                        .artistFormList(artistList)
                        .groupForm(new GroupForm(groupName, groupDescription))
                        .build();

                musicFormList.add(musicForm);
            }
            System.out.println("musicFormList = " + musicFormList);
            playlistService.create(playlistForm, musicFormList);
        }


        
    }
    public String readCell(XSSFRow row, int cellNum) {
        XSSFCell cell = row.getCell(cellNum);
        if(cell == null)
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
            case ERROR: // 에러난 처리
                value = cell.getErrorCellValue() + "";
                break;
        }

        return value;
    }

}


