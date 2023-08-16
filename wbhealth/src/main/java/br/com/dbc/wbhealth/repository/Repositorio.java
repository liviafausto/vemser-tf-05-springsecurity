package br.com.dbc.wbhealth.repository;


import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Repositorio<Key, T> {
    T save(T entidade) throws BancoDeDadosException;

    List<T> findAll() throws BancoDeDadosException;

    T findById(Key id) throws BancoDeDadosException, EntityNotFound;

    T update(Key id, T entidadeAtualizada) throws BancoDeDadosException, EntityNotFound;

    boolean deleteById(Key id) throws BancoDeDadosException, EntityNotFound;

    Integer getProximoId(Connection connection, String nextSequence) throws SQLException;

}
