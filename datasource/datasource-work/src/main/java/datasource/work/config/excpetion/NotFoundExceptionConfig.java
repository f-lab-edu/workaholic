package datasource.work.config.excpetion;

import datasource.work.config.excpetion.type.NotFoundProjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundExceptionConfig {
    @ExceptionHandler(NotFoundProjectException.class)
    protected ResponseEntity<ExceptionResponse> handleNotFoundProjectException(NotFoundProjectException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), "해당 프로젝트는 존재하지 않습니다."));
    }
}
