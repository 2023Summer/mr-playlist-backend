package summer.mrplaylist.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupForm {
	private String groupName;
	private String groupDescription;
	private String imgUrl;
}
