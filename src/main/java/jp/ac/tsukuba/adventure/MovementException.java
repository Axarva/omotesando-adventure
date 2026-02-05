package jp.ac.tsukuba.adventure;

// Error
// Custom exception for movement errors
class MovementException extends Exception {
    public MovementException(String message) {
        super(message);
    }
}