package com.server.gateway.service.custom.exceptions;

public class UnknownException extends RuntimeException {
    private short errorCode;
    private String errorMessage;

    public UnknownException() {
    }

    public short getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
