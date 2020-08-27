package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.EException;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface DAO<T> {

    public T insert(T object) throws EException;

    public T update(T object) throws EException;

    public void delete(int... identifiers) throws EException;

    public T selectBy(String query, Collection<Object> fieldsValues) throws EException;

    public T selectById(int... identifiers) throws EException;

    public T selectByField(String field, Object fieldValue) throws EException;

    public List<T> selectAll() throws EException;

    public List<T> selectAllBy(String query, Collection<Object> fieldsValues) throws EException;

    public List<T> selectAllByField(String field, Object fieldValue) throws EException;
}

