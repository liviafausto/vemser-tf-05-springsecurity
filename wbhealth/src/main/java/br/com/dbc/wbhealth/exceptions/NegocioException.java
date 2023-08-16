package br.com.dbc.wbhealth.exceptions;

public class NegocioException extends RuntimeException {

    public NegocioException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public NegocioException(String message) {
        super(message);
    }
}
