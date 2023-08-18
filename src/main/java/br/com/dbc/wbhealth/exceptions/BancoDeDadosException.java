package br.com.dbc.wbhealth.exceptions;

import java.sql.SQLException;

public class BancoDeDadosException extends SQLException {
    public BancoDeDadosException(String cause) {
        super(cause);
    }
}
