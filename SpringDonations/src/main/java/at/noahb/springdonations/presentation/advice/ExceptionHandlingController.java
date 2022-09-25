package at.noahb.springdonations.presentation.advice;

import at.noahb.springdonations.exception.NotFoundException;
import at.noahb.springdonations.payload.response.NotFoundResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<NotFoundResponse> handlePersonNotFoundException(NotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new NotFoundResponse(NOT_FOUND.value(), e.getMessage()));
    }
}
