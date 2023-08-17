package summer.mrplaylist.common.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Response<T> {

	private LocalDateTime date;
	private T data;

	public Response(T data) {
		this.date = LocalDateTime.now();
		this.data = data;
	}
}
