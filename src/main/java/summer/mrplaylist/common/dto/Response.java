package summer.mrplaylist.common.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Response<T> {

	private LocalDateTime date;
	private List<T> data;

	public Response(T data) {
		this.date = LocalDateTime.now();
		this.data.add(data);
	}

	public Response(List<T> data) {
		this.date = LocalDateTime.now();
		this.data = data;
	}
}
