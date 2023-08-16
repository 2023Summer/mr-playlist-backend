package summer.mrplaylist.common.config.jwt;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import summer.mrplaylist.common.dto.ErrorResponse;
import summer.mrplaylist.common.exception.TokenNotValidateException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (TokenNotValidateException e) {
			setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
		}
	}

	private void setErrorResponse(HttpStatus status, HttpServletResponse response, TokenNotValidateException e) throws
		IOException {
		response.setStatus(status.value());
		response.setContentType("application/json; charset=UTF-8");

		response.getWriter().print(
			new ErrorResponse(
				LocalDateTime.now(),
				e.getMessage()
			)
				.convertToJson()
		);
	}
}

