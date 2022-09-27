package at.noahb.employeetask.presentation;

import at.noahb.employeetask.exceptions.EmployeeAlreadyExistsException;
import at.noahb.employeetask.exceptions.EmployeeNotFoundException;
import at.noahb.employeetask.payload.response.ErrorMessage;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.time.DateTimeException;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> databaseError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database Error");
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleEmployeeNotFund() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Employee not found"));
    }

    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleDateTimeException(DateTimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleEmployeeAlreadyExists(EmployeeAlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(new ErrorMessage(exception.getMessage()));
    }
}
