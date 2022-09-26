package at.noahb.springdonations.presentation.advice;

import at.noahb.springdonations.exception.NotFoundException;
import at.noahb.springdonations.payload.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ExceptionHandlingController {

    private static final Logger LOGGER = LoggerFactory.getLogger("ExceptionHandlingController");

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handlePersonNotFoundException(NotFoundException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(new ExceptionResponse(NOT_FOUND.value(), NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(BAD_REQUEST.value(), BAD_REQUEST, exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList().toString()));
    }

    @ExceptionHandler({SQLException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleSQLException(SQLException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(BAD_REQUEST.value(), BAD_REQUEST, "Database error!"));
    }
}
