package xyz.smsverification.exception;

public class InvalidCredentialsException extends ApiErrorException {

    public InvalidCredentialsException(String msg) {
        super(msg);
    }
}
