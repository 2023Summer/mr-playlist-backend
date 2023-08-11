package summer.mrplaylist.common.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorResponse {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private LocalDateTime timestamp;
	private String message;

	public String convertToJson() throws JsonProcessingException {
		return objectMapper.writeValueAsString(this);
	}

}
