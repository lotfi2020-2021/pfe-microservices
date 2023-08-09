package esprit.tn.exception;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException() {
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}
