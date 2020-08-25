package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.dal.DAO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GenericJDBCDAOImpl<T> implements DAO<T> {
    protected List<String> identifiers;
    protected Map<String, String> fields;
    protected Class<T> entityClass;
    protected String SQL_DELETE;
    protected String SQL_SELECT_BY_ID;
    protected String SQL_SELECT_ALL;

    // CONSTRUCTOR

    public GenericJDBCDAOImpl() throws EException {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        setIdentifiers();
        setFields();
        setSQL_DELETE();
        setSQL_SELECT_BY_ID();
        setSQL_SELECT_ALL();
    }


    // ABSTRACT METHODS

    protected abstract void setIdentifiers();

    protected abstract void setFields();

    protected abstract T getObject();


    // CRUD METHODS

    @Override
    public void insert(T object) throws EException {
    }

    @Override
    public T update(T object) throws EException {
        return null;
    }

    /**
     * @param identifiers int | Identifier. Requires two parameters when deleting an "Enchere" element: "articleVendu" and "encherisseur".
     * @throws EException EException | CRUD_DELETE_ERROR.
     */
    @Override
    public void delete(int... identifiers) throws EException {
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(this.SQL_DELETE);
            setStatementIdentifiers(statement, identifiers);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_DELETE_ERROR.get(this.getActualClassName()), sqlException);
        }
    }

    /**
     * @param identifiers int | Identifier. Requires two parameters when deleting an "Enchere" element: "articleVendu" and "encherisseur".
     * @return T | Instance of generic object with the provided identifier(s).
     * @throws EException EException | CRUD_SELECT_ID_ERROR.
     */
    @Override
    public T selectById(int... identifiers) throws EException {
        T instance = null;
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(this.SQL_SELECT_BY_ID);
            setStatementIdentifiers(statement, identifiers);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) { instance = generateObject(resultSet); }

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_SELECT_ID_ERROR.get(this.getActualClassName()), exception);
        }
        return instance;
    }

    /**
     * @return List<T> | List of all the instances of generic objects.
     * @throws EException EException | CRUD_SELECT_ALL_ERROR.
     */
    @Override
    public List<T> selectAll() throws EException {
        List<T> instances = new ArrayList<>();
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(this.SQL_SELECT_ALL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) { instances.add(generateObject(resultSet)); }
            return instances;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_SELECT_ALL_ERROR.get(this.getActualClassName()), exception);
        }
    }


    // HELPER METHODS

    /**
     * @param resultSet | ResultSet
     * @return T | Instance of generic object with the data of the ResultSet.
     * @throws EException EException | GENERATE_OBJECT_ERROR.
     */
    protected T generateObject(ResultSet resultSet) throws EException {
        T instance = getObject();
        int index = 0;
        try {
            for (Map.Entry<String, String> field : this.fields.entrySet()) {
                String method = "set" + field.getKey().substring(0, 1).toUpperCase() + field.getKey().substring(1);
                switch (field.getValue()) {
                    case "String":
                        entityClass.getMethod(method, String.class).invoke(instance, resultSet.getString(field.getKey()));
                        break;
                    case "int":
                        entityClass.getMethod(method, int.class).invoke(instance, resultSet.getInt(field.getKey()));
                        break;
                    case "Date":
                        entityClass.getMethod(method, LocalDate.class).invoke(instance, resultSet.getDate(field.getKey()));
                        break;
                    case "byte":
                        entityClass.getMethod(method, byte.class).invoke(instance, resultSet.getByte(field.getKey()));
                        break;
                }
                index ++;
            }
        } catch (IllegalAccessException | SQLException | InvocationTargetException | NoSuchMethodException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.GENERATE_OBJECT_ERROR.get(this.getActualClassName()), exception);
        }
        return instance;
    }

    protected String getActualClassName() throws EException {
        return this.entityClass.getSimpleName();
    }

    protected void setStatementIdentifiers(PreparedStatement statement, int... identifiers) throws EException {
        try {
            for (int parameterIndex = 0; parameterIndex < identifiers.length; parameterIndex ++) {
                statement.setInt(parameterIndex + 1, identifiers[parameterIndex]);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new EException(CodesExceptionJDBC.IDENTIFIER_STATEMENT_ERROR, sqlException);
        }
    }

    protected String getIdentifiersConditions() {
        StringBuilder identifiersConditions = new StringBuilder();
        for (int index = 0; index < this.identifiers.size(); index ++) {
            identifiersConditions.append(identifiers.get(index)).append(" = ?");
            if (index != this.identifiers.size() - 1) {
                identifiersConditions.append(" AND ");
            }
        }
        return identifiersConditions.toString();
    }

    protected String getFieldsSelection() {
        StringBuilder fieldsSelection = new StringBuilder();
        String separator = "";
        for (Map.Entry<String, String> field : this.fields.entrySet()) {
            fieldsSelection.append(separator).append(field.getKey());
            separator = ", ";
        }
        return fieldsSelection.toString();
    }


    // GETTERS & SETTERS

    protected void setSQL_DELETE() throws EException {
        this.SQL_DELETE = "DELETE FROM " + getActualClassName() + " WHERE " + getIdentifiersConditions();
    }

    protected void setSQL_SELECT_BY_ID() throws EException {
        this.SQL_SELECT_BY_ID = "SELECT " + getFieldsSelection() + " FROM " + getActualClassName() + " WHERE " + getIdentifiersConditions();
    }

    protected void setSQL_SELECT_ALL() throws EException {
        this.SQL_SELECT_ALL = "SELECT " + getFieldsSelection() + " FROM " + getActualClassName();
    }
}
