package summer.mrplaylist.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupForm {
	private String groupName;
	private String groupDescription;
	private String imgUrl;
}
