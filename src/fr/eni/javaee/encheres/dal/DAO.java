package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.EException;

import java.util.List;

public interface DAO<T> {

    public void insert(T object) throws EException;

    public T update(T object) throws EException;

    public void delete(int... identifiers) throws EException;

    public T selectById(int... identifiers) throws EException;

    public List<T> selectAll() throws EException;
}

