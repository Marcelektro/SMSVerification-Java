package xyz.smsverification.exception;

public class WaitingForMessagesException extends ApiErrorException {

    public WaitingForMessagesException(String msg) {
        super(msg);
    }
}
