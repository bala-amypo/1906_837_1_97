@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(com.example.demo.exception.ResourceNotFoundException.class)
    public ResponseEntity<String> handle404(Exception e) {
        return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle400(Exception e) {
        return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.BAD_REQUEST);
    }
}