package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.DAO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class GenericJDBCDAOImpl<T> implements DAO<T> {
    protected List<String> identifiers;
    protected Map<String, String> fields;
    protected Class<T> entityClass;
    protected String SQL_DELETE;
    protected String SQL_SELECT_BY_ID;
    protected String SQL_SELECT_ALL;
    protected String SQL_INSERT;
    protected String SQL_UPDATE;

    // CONSTRUCTOR

    public GenericJDBCDAOImpl() throws EException {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        setIdentifiers();
        setFields();
        setSQL_DELETE();
        setSQL_SELECT_BY_ID();
        setSQL_SELECT_ALL();
        setSQL_UPDATE();
        setSQL_INSERT(this.fields.size());
    }


    // ABSTRACT METHODS

    protected abstract void setIdentifiers();

    protected abstract void setFields();

    protected abstract T getObject();


    // CRUD METHODS

    /**
     * <!> This method can not be used with Enchere and Retrait.
     * @param object T | Instance of the actual object to insert into the database.
     * @return T | Instance of the actual  object sucessfully inserted into the database.
     * @throws EException EEXception | CRUD_INSERT.
     */
    @Override
    public T insert(T object) throws EException {
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(this.SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            generateStatementData(statement, object);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                String identifier = this.identifiers.get(0);
                String method = "set" + identifier.substring(0, 1).toUpperCase() + identifier.substring(1);
                entityClass.getMethod(method, int.class).invoke(object, resultSet.getInt(1));
            }
        } catch (IllegalAccessException | SQLException | NoSuchMethodException | InvocationTargetException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_INSERT.get(this.getActualClassName()), exception);
        }
        return object;
    }

    /**
     * @param object T | Instance of generic object to update into the database.
     * @return T | Instance of the actual object sucessfully updated into the database.
     * @throws EException EEXception | CRUD_UPDATE.
     */
    @Override
    public T update(T object) throws EException {
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(this.SQL_UPDATE);
            generateStatementData(statement, object, true);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_UPDATE.get(this.getActualClassName()), sqlException);
        }
        return object;
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
     * @return T | Instance of the actual object with the provided identifier(s).
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
     * @return List<T> | List of all the instances of the actual objects.
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
     * @return T | Instance of the actual object with the data of the ResultSet.
     * @throws EException EException | GENERATE_OBJECT_ERROR.
     */
    protected T generateObject(ResultSet resultSet) throws EException {
        T instance = getObject();
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
                        entityClass.getMethod(method, LocalDate.class).invoke(instance, resultSet.getDate(field.getKey()).toLocalDate());
                        break;
                    case "byte":
                        entityClass.getMethod(method, byte.class).invoke(instance, resultSet.getByte(field.getKey()));
                        break;
                    default:
                        int identifier = resultSet.getInt(field.getKey());
                        GenericJDBCDAOImpl<T> implementation = (GenericJDBCDAOImpl<T>) Class.forName(field.getValue()).newInstance();
                        entityClass.getMethod(method, implementation.getObject().getClass()).invoke(instance, implementation.selectById(identifier));
                }
            }
        } catch (IllegalAccessException | SQLException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | InstantiationException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.GENERATE_OBJECT_ERROR.get(this.getActualClassName()), exception);
        }
        return instance;
    }

    /**
     * @param statement PreparedStatement | Statement in which the data will be added.
     * @param object T | Instance of the actual object providing the data.
     * @param update boolean | "true" if the statement is about an update.
     * @throws EException EException | GENERATE_STATEMENT_DATA_ERROR.
     */
    protected void generateStatementData(PreparedStatement statement, T object, boolean update) throws EException {
        try {
            int parameterIndex = 1;
            for (Map.Entry<String, String> field : this.fields.entrySet()) {
                String method = "get" + field.getKey().substring(0, 1).toUpperCase() + field.getKey().substring(1);
                if (this.identifiers.contains(field.getKey())) { continue; } // Auto-generated.
                switch (field.getValue()) {
                    case "String":
                        statement.setString(parameterIndex, (String) entityClass.getMethod(method).invoke(object));
                        break;
                    case "int":
                        statement.setInt(parameterIndex, (int) entityClass.getMethod(method).invoke(object));
                        break;
                    case "Date":
                        statement.setDate(parameterIndex, Date.valueOf((LocalDate) entityClass.getMethod(method).invoke(object)));
                        break;
                    case "byte":
                        statement.setByte(parameterIndex, (byte) entityClass.getMethod(method).invoke(object));
                        break;
                    default:
                        method = "getNo" + field.getKey().substring(0, 1).toUpperCase() + field.getKey().substring(1);
                        System.out.println(method);
                        statement.setInt(parameterIndex, (int) entityClass.getMethod(method).invoke(object));
                        break;
                }
                parameterIndex++;
            }
            if (update) {
                for (String identifier : this.identifiers) {
                    String method = "get" + identifier.substring(0, 1).toUpperCase() + identifier.substring(1);
                    statement.setInt(parameterIndex, (int) entityClass.getMethod(method).invoke(object));
                    parameterIndex ++;
                }
            }
        } catch (InvocationTargetException | NoSuchMethodException | SQLException | IllegalAccessException exception){
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.GENERATE_STATEMENT_DATA_ERROR.get(this.getActualClassName()), exception);
        }
    }

    protected void generateStatementData(PreparedStatement statement, T object) throws EException {
        generateStatementData(statement, object, false); // Default value.
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

    /**
     *
     * @param isUnknownParameter boolean | "true" if the field is an unknown parameter in the query.
     * @return String | List of fields of the actual object.
     */
    protected String getFieldsSelection(boolean isUnknownParameter) {
        StringBuilder fieldsSelection = new StringBuilder();
        String separator = "";
        for (Map.Entry<String, String> field : this.fields.entrySet()) {
            if (this.identifiers.contains(field.getKey()) && isUnknownParameter) { continue; }
            fieldsSelection.append(separator).append(field.getKey());
            if (isUnknownParameter) { fieldsSelection.append(" = ?"); }
            separator = ", ";
        }
        return fieldsSelection.toString();
    }

    protected String getFieldsSelection() {
        return getFieldsSelection(false); // Default value.
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

    protected void setSQL_INSERT(int length) throws EException {
        StringBuilder unknownParameters = new StringBuilder();
        String separator = "";
        while (-- length > 0) {
            unknownParameters.append(separator).append("?");
            separator = ", ";
        }
        this.SQL_INSERT = "INSERT INTO " + getActualClassName() + " VALUES ( " + unknownParameters.toString() + " ) ";
    }

    protected void setSQL_UPDATE() throws EException {
        this.SQL_UPDATE = "UPDATE " + getActualClassName() + " SET " + getFieldsSelection(true) + " WHERE " + getIdentifiersConditions();
    }
}
