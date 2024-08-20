package workaholic.config.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ApiResponse<T> {
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final T data;

    public ApiResponse(T data) {
        this.data = data;
    }

    public static ResponseEntity<Void> success() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<T>(data));
    }

    public static ResponseEntity<ApiResponse<List<String>>> fail(HttpStatusCode statusCode) {
        return ResponseEntity
                .status(statusCode)
                .build();
    }
}
