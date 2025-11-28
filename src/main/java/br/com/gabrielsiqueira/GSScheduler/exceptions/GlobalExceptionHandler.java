package br.com.gabrielsiqueira.GSScheduler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionResponse> handleAll(Exception ex, WebRequest req) {
        CustomExceptionResponse err = new CustomExceptionResponse();
        err.setTimeStamp(new Date());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
