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

public class GenericJDBCDAO<T> implements DAO<T> {
    protected Class<T> entityClass;
    protected String identifier;
    protected final String SQL_DELETE =
            "DELETE FROM" + entityClass.getSimpleName() + " WHERE " + identifier + " = ?";
    protected final String SQL_SELECT_BY_ID =
            "SELECT * FROM" + entityClass.getSimpleName() + " WHERE " + identifier + " = ?";
    protected final String SQL_SELECT_ALL = "SELECT * FROM" + entityClass.getSimpleName();



    public GenericJDBCDAO() throws EException {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        try {
            Method getIdentifierNamedentifier = this.entityClass.getClass().getMethod("getIdentifierName");
            identifier = (String) getIdentifierNamedentifier.invoke(this.entityClass.getClass());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            throw new EException(CodesExceptionJDBC.IDENTIFIER_ERROR, exception);
        }
    }

    @Override
    public void insert(T object) throws EException {

    }

    @Override
    public void delete(int identifier) throws EException {
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setInt(1, identifier);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new EException(CodesExceptionJDBC.CRUD_DELETE_ERROR, sqlException);
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
}
