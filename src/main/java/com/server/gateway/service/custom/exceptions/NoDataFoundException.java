package com.server.gateway.service.custom.exceptions;

public class NoDataFoundException extends RuntimeException {
    private short errorCode;
    private String errorMessage;

    public NoDataFoundException() {
    }

    public NoDataFoundException(short errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
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
