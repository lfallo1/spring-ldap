package com.lancefallon.superhero;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lancefallon.superhero.exception.InvalidCredentialsException;

/**
 * let certain errors bubble up to be handled here.
 * <br>
 * all methods return a ResponseEntity of a specified type as well as a status code
 * @author lancefallon
 *
 */
@ControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler({InvalidCredentialsException.class})
    ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) throws IOException {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
