package summer.mrplaylist.common.dto;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class PageResponse<T> extends Response {
	private PageInfo pageInfo;

	public PageResponse(Page<T> page) {
		super(page.getContent());

		this.pageInfo = PageInfo.builder()
			.pageNumber(page.getNumber() + 1)
			.pageSize(page.getSize())
			.totalPages(page.getTotalPages())
			.totalElements(page.getTotalElements())
			.first(page.isFirst())
			.last(page.isLast())
			.build();
	}

	@Override
	public String toString() {
		return "PageResponse{" +
			"date=" + this.getDate() +
			"data=" + this.getData() +
			"pageInfo=" + pageInfo +
			'}';
	}

	@Builder
	@Data
	static class PageInfo {
		private int pageNumber;
		private int pageSize;
		private int totalPages;
		private long totalElements;
		private boolean first;
		private boolean last;
	}
}
