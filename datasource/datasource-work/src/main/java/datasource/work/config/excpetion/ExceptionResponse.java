package datasource.work.config.excpetion;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final String reasonPhrase;
    private final String message;

    public ExceptionResponse(String reasonPhrase, String message) {
        this.reasonPhrase = reasonPhrase;
        this.message = message;
    }
}
