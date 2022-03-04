package xyz.smsverification.exception;

public class OrderUnavailableException extends ApiErrorException {

    public OrderUnavailableException(String msg) {
        super(msg);
    }
}
