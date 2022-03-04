package xyz.smsverification.exception;

public class InvalidApiResponseException extends ApiErrorException {

    public InvalidApiResponseException(String msg) {
        super(msg);
    }
}
