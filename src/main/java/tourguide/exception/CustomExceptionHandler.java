package tourguide.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;


import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex, ServletWebRequest request){
        return new ErrorResponse( HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), ex.getMessage());
    }

    @ExceptionHandler(UnAuthorizeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnAuthorizeException(UnAuthorizeException ex, ServletWebRequest request){
        return new ErrorResponse( HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now(), ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException ex, ServletWebRequest request){
        return new ErrorResponse( HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), ex.getMessage());
    }
}