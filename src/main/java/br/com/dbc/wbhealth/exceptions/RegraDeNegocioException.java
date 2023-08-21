package br.com.dbc.wbhealth.exceptions;

public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RegraDeNegocioException(String message) {
        super(message);
    }
}
