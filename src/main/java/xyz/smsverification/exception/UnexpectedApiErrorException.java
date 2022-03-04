package xyz.smsverification.exception;

public class UnexpectedApiErrorException extends ApiErrorException {

    public UnexpectedApiErrorException(String msg) {
        super(msg);
    }
}
