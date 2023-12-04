package uz.travellog.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.travellog.dto.response.Response;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> notValidException(MethodArgumentNotValidException e){
        return ResponseEntity
                .status(400)
                .body(
                        new Response(
                                400,
                                e.getFieldErrors().stream().map(a->a.getField() +": " +a.getDefaultMessage()).toList().toString(),
                            null)
                );
    }
}
