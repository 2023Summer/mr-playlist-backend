package summer.mrplaylist.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SearchResponse {
	private Long id;
	private String name;
	private String description;
}
