package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Utilisateur;

import java.util.List;

public interface DAO<T> {

    public T insert(T object) throws EException;

    public T update(T object) throws EException;

    public void delete(int... identifiers) throws EException;

    public T selectById(int... identifiers) throws EException;

    public T selectByField(String field, Object fieldValue) throws EException;

    public List<T> selectAll() throws EException;

    public List<T> selectAllByField(String field, Object fieldValue) throws EException;

}

