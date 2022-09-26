package at.noahb.springdonations.payload.response;


import org.springframework.http.HttpStatus;

public record ExceptionResponse(int code, HttpStatus status, String message) {

}
