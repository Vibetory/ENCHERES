package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.dal.DAO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GenericJDBCDAOImpl<T> implements DAO<T> {
    protected Class<T> entityClass;
    protected final String SQL_DELETE;
    protected final String SQL_SELECT_BY_ID;
    protected final String SQL_SELECT_ALL;

    public GenericJDBCDAOImpl() throws EException {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.SQL_DELETE = "DELETE FROM " + getActualClassName() + " WHERE " + getIdentifier() + " = ?";
        this.SQL_SELECT_BY_ID = "SELECT * FROM " + getActualClassName() + " WHERE " + getIdentifier() + " = ?";
        this.SQL_SELECT_ALL = "SELECT * FROM " + getActualClassName();
    }

    @Override
    public void insert(T object) throws EException {
    }

    @Override
    public T update(T object) throws EException {
        return null;
    }

    @Override
    public void delete(int identifier) throws EException {
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setInt(1, identifier);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_DELETE_ERROR.get(this.getActualClassName()), sqlException);
        }
    }

    @Override
    public T selectById(int identifier) throws EException {
        return null;
    }

    @Override
    public List<T> selectAll() throws EException {
        return null;
    }

    public String getIdentifier() throws EException {
        try {
            Method getIdentifierName= this.entityClass.getMethod("getIdentifierName");
            return (String) getIdentifierName.invoke(this.entityClass);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.IDENTIFIER_ERROR, exception);
        }
    }

    public String getActualClassName() throws EException {
        return this.entityClass.getSimpleName();
    }
}
