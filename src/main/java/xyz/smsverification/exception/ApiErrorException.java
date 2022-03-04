package xyz.smsverification.exception;

import lombok.Getter;

@Getter
public abstract class ApiErrorException extends Exception {
    private final String msg;

    public ApiErrorException(String msg) {
        this.msg = msg;
    }
}
