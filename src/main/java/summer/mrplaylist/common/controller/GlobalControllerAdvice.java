package summer.mrplaylist.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import summer.mrplaylist.common.dto.ErrorResponse;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorResponse exception(Exception e){
        log.error("error : {}",e.getStackTrace());
        return createErrorResponse(e.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {IllegalStateException.class,IllegalArgumentException.class})
    public ErrorResponse badRequest(RuntimeException e){
        log.info("badRequest error: {}",e.getStackTrace());
        return createErrorResponse(e.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public ErrorResponse badCredentialsException(BadCredentialsException e){
        log.info("BadCredentialsException error: {}",e.getStackTrace());
        return createErrorResponse(e.getMessage());
    }

    private static ErrorResponse createErrorResponse(String message) {
        return new ErrorResponse(LocalDateTime.now(), message);
    }


}