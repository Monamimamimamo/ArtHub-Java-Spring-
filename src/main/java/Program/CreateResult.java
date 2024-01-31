package Program;

import org.springframework.http.HttpStatus;

public class CreateResult {
    private final HttpStatus status;
    private final String message;

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public CreateResult(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
