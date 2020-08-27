package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.dal.DAO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public abstract class GenericJDBCDAOImpl<T> implements DAO<T> {
    protected List<String> identifiers;
    protected int autogeneratedIdentifiers;
    protected Map<String, String> fields;
    protected Class<T> entityClass;


    // CONSTRUCTOR

    protected GenericJDBCDAOImpl() throws EException {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        setIdentifiers();
        setFields();
    }


    // ABSTRACT METHODS

    /**
     * The "identifiers" are the field use for the selectById() methods.
     * The "autogeneratedIdentifiers" attribute is the number of auto-generated identifiers.
     */
    protected abstract void setIdentifiers();

    /**
     * The "fields" attribute must be a LinkedHashMap with the names of the fields as keys, and the data type as attribute.
     * If the data type is a Business Object, the value must be the path to its DAO's implementation.
     */
    protected abstract void setFields();

    protected abstract T getObject();


    // CRUD METHODS

    // INSERT OR UPDATE
    /**
     * @param object T | Instance of the actual object to insert or update into the database.
     * @param query String | SQL Query.
     * @param isUpdate boolean | "true" if the operation is an update, "false" if it is an insert.
     * @return T | Instance of the actual object successfully inserted or updated into the database.
     * @throws EException
     */
    private T insertOrUpdate(T object, String query, boolean isUpdate) throws EException {
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            setStatementParameters(statement, object, isUpdate);
            statement.executeUpdate();
            if (!isUpdate) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (this.autogeneratedIdentifiers > 0 && resultSet.next()) {
                    String identifier = this.identifiers.get(0);
                    String method = "set" + identifier.substring(0, 1).toUpperCase() + identifier.substring(1);
                    entityClass.getMethod(method, int.class).invoke(object, resultSet.getInt(1));
                }
                resultSet.close();
            }
            statement.close();
        } catch (IllegalAccessException | SQLException | NoSuchMethodException | InvocationTargetException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_INSERT_OR_UPDATE_ERROR, exception);
        }
        return object;
    }

    // INSERT
    /**
     * @param object T | Instance of the actual object to insert into the database.
     * @return T | Instance of the actual  object successfully inserted into the database.
     * @throws EException EEXception | CRUD_INSERT.
     */
    @Override
    public T insert(T object) throws EException {
        int numberOfParameters = this.fields.size() - this.autogeneratedIdentifiers;
        String SQL_INSERT = TransactSQLQueries.INSERT(getActualClassName(), numberOfParameters);
        try { insertOrUpdate(object, SQL_INSERT, false); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_INSERT.get(this.getActualClassName()), eException);
        }
        return object;
    }

    // UPDATE
    /**
     * @param object T | Instance of generic object to update into the database.
     * @return T | Instance of the actual object successfully updated into the database.
     * @throws EException EEXception | CRUD_UPDATE.
     */
    @Override
    public T update(T object) throws EException {
        String SQL_UPDATE = TransactSQLQueries.UPDATE(
                getActualClassName(),
                generateQueryFields(this.fields.keySet(), true, false, true),
                generateQueryFields(new LinkedHashSet<>(this.identifiers), true, true, false)
        );
        try { insertOrUpdate(object, SQL_UPDATE, true); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_UPDATE.get(this.getActualClassName()), eException);
        }
        return object;
    }

    // DELETE
    /**
     * @param identifiers int | Identifier. Requires two parameters when deleting an "Enchere" element: "articleVendu" and "encherisseur".
     * @throws EException EException | CRUD_DELETE_ERROR.
     */
    @Override
    public void delete(int... identifiers) throws EException {
        Map<String, Object> fields = generateIdentifiersMap(identifiers);
        String SQL_DELETE = TransactSQLQueries.DELETE(getActualClassName(), generateQueryFields(fields.keySet(), true, true, false));
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            setStatementParameters(statement, fields.values());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_DELETE_ERROR.get(this.getActualClassName()), sqlException);
        }
    }


    // SELECT T

    /**
     * @param query String | SQL Query
     * @param fieldsValues Collection<Object> | Fields values to implement in the query.
     * @return T | Instance of the actual object.
     * @throws EException EException
     */
    @Override
    public T selectBy(String query, Collection<Object> fieldsValues) throws EException {
        try { return selectAllBy(query, fieldsValues).get(0); } // Get the first element of the list.
        catch (EException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_SELECT_BY_ERROR, exception);
        }
    }

    /**
     * @param fields Map<String, Object> | Map of the fields as keys and their values as values.
     * @return T | Instance of the actual object with the provided identifier(s).
     * @throws EException EException | CRUD_SELECT_FIELD_ERROR.
     * @throws SQLException SQLException
     */
    @Override
    public T selectByFields(Map<String, Object> fields) throws EException {
        String SQL_SELECT_BY_FIELDS = TransactSQLQueries.SELECT(
                generateQueryFields(),
                getActualClassName(),
                generateQueryFields(fields.keySet(), true, true, false)
        );
        return selectBy(SQL_SELECT_BY_FIELDS, fields.values());
    }

    /**
     * @param identifiers int | Identifier. Must be used with two parameters when selecting an "Enchere" element: "articleVendu" and "encherisseur".
     * @return T | Instance of the actual object with the provided identifier(s).
     * @throws EException EException | CRUD_SELECT_ID_ERROR.
     */
    @Override
    public T selectById(int... identifiers) throws EException {
        Map<String, Object> fields = generateIdentifiersMap(identifiers);
        try { return selectByFields(fields); }
        catch (EException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_SELECT_ID_ERROR.get(this.getActualClassName()), exception);
        }
    }

    /**
     * @param field String | Name of the field used as a conditional parameter.
     * @param fieldValue Object | Value of the field.
     * @return T | Instance of the actual object with the provided identifier(s).
     * @throws EException EException | CRUD_SELECT_FIELD_ERROR.
     */
    @Override
    public T selectByField(String field, Object fieldValue) throws EException {
        Map<String, Object> fields = new HashMap<String, Object>() {{
            put(field, fieldValue);
        }};
        try { return selectByFields(fields); }
        catch (EException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_SELECT_FIELD_ERROR.get(this.getActualClassName()), exception);
        }
    }


    // SELECT LIST<T>

    /**
     * @return List<T> | List of all the instances of the actual object.
     * @throws EException EException | CRUD_SELECT_ALL_ERROR.
     */
    @Override
    public List<T> selectAll() throws EException {
        List<T> instances = new ArrayList<>();
        String SQL_SELECT_ALL = TransactSQLQueries.SELECT_ALL(generateQueryFields(), getActualClassName());
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) { instances.add(generateObject(resultSet)); }
            resultSet.close();
            statement.close();
            return instances;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_SELECT_ALL_ERROR.get(this.getActualClassName()), exception);
        }
    }

    /**
     *
     * @param query String | SQL Query
     * @param fieldsValues Collection<Object> | Fields values to implement in the query.
     * @return List<T> | List of instances of the actual object.
     * @throws EException EEXception
     */
    @Override
    public List<T> selectAllBy(String query, Collection<Object> fieldsValues) throws EException {
        List<T> instances = new ArrayList<>();
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            setStatementParameters(statement, fieldsValues);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) { instances.add(generateObject(resultSet)); }
            resultSet.close();
            statement.close();
            return instances;
        } catch (EException | SQLException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_SELECT_ALL_BY_ERROR, exception);
        }
    }

    /**
     * @param fields Map<String, Object> | Map of the fields as keys and their values as values.
     * @return List<T> | List of instances of the actual object.
     * @throws EException EEXception
     * @throws SQLException SQLException
     */
    @Override
    public List<T> selectAllByFields(Map<String, Object> fields) throws EException {
        String SQL_SELECT_ALL_BY_FIELDS = TransactSQLQueries.SELECT_ALL(generateQueryFields(), getActualClassName());
        return selectAllBy(SQL_SELECT_ALL_BY_FIELDS, fields.values());
    }

    /**
     * @param field String | Name of the field used as a conditional parameter.
     * @param fieldValue Object | Value of the field.
     * @return T | Instance of the actual object with the provided identifier(s).
     * @throws EException EException | CRUD_SELECT_FIELD_ERROR.
     */
    @Override
    public List<T> selectAllByField(String field, Object fieldValue) throws EException {
        Map<String, Object> fields = new HashMap<String, Object>() {{
            put(field, fieldValue);
        }};
        try { return selectAllByFields(fields); }
        catch (EException exception) {
            exception.printStackTrace();
            throw new EException(CodesExceptionJDBC.CRUD_SELECT_FIELD_ERROR.get(this.getActualClassName()), exception);
        }
    }


    // METHODS

    /**
     * @param resultSet | ResultSet
     * @return T | Instance of the actual object with the data of the ResultSet.
     * @throws EException EException | GENERATE_OBJECT_ERROR.
     */
    private T generateObject(ResultSet resultSet) throws EException {
        T instance = getObject();
        try {
            for (Map.Entry<String, String> field : this.fields.entrySet()) {
                String method = "set" + field.getKey().substring(0, 1).toUpperCase() + field.getKey().substring(1);
                switch (field.getValue()) {
                    case "String":
                        entityClass.getMethod(method, String.class).invoke(instance, resultSet.getString(field.getKey()));
                        break;
                    case "Integer":
                        entityClass.getMethod(method, int.class).invoke(instance, resultSet.getInt(field.getKey()));
                        break;
                    case "LocalDateTime":
                        entityClass.getMethod(method, LocalDateTime.class).invoke(instance, resultSet.getTimestamp(field.getKey()).toLocalDateTime());
                        break;
                    case "Byte":
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
     * @param isUpdate boolean | "true" if the statement is about an update.
     * @throws EException EException | GENERATE_STATEMENT_DATA_ERROR.
     */
    private void setStatementParameters(PreparedStatement statement, T object, boolean isUpdate) throws EException {
        try {
            int parameterIndex = 1;
            int skips = this.autogeneratedIdentifiers;
            for (Map.Entry<String, String> field : this.fields.entrySet()) {
                if (skips != 0) { skips --; continue; }
                String method = "get" + field.getKey().substring(0, 1).toUpperCase() + field.getKey().substring(1);
                switch (field.getValue()) {
                    case "String":
                        statement.setString(parameterIndex, (String) entityClass.getMethod(method).invoke(object));
                        break;
                    case "Integer":
                        statement.setInt(parameterIndex, (int) entityClass.getMethod(method).invoke(object));
                        break;
                    case "LocalDateTime":
                        LocalDateTime localDateTime = (LocalDateTime) entityClass.getMethod(method).invoke(object);
                        statement.setTimestamp(parameterIndex, Timestamp.valueOf(localDateTime));
                        break;
                    case "Byte":
                        statement.setByte(parameterIndex, (byte) entityClass.getMethod(method).invoke(object));
                        break;
                    default:
                        method = "getNo" + field.getKey().substring(0, 1).toUpperCase() + field.getKey().substring(1);
                        statement.setInt(parameterIndex, (int) entityClass.getMethod(method).invoke(object));
                        break;
                }
                parameterIndex++;
            }
            if (isUpdate) {
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

    /**
     *
     * @param statement PreparedStatement | Statement in which the data will be added.
     * @param fieldsValues Collection<Object> | Collection of field values.
     * @throws EException EException | GENERATE_STATEMENT_DATA_ERROR.
     */
    private void setStatementParameters(PreparedStatement statement, Collection<Object> fieldsValues) throws EException {
        try {
            int parameterIndex = 1;
            for (Object fieldValues : fieldsValues) {
                if (fieldValues instanceof  String) { statement.setString(parameterIndex, (String) fieldValues); }
                else if (fieldValues instanceof  Integer) { statement.setInt(parameterIndex, (int) fieldValues); }
                else if (fieldValues  instanceof  LocalDateTime) {
                    statement.setTimestamp(parameterIndex, Timestamp.valueOf((LocalDateTime) fieldValues));
                }
                else if (fieldValues  instanceof  Byte) { statement.setByte(parameterIndex, (byte) fieldValues); }
                parameterIndex ++;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new EException(CodesExceptionJDBC.GENERATE_STATEMENT_DATA_ERROR.get(this.getActualClassName()), sqlException);
        }
    }

    private String getActualClassName() { return this.entityClass.getSimpleName(); }

    /**
     * @param fields Set<String> | Set of fields.
     * @param isUnknownParameter boolean | "true" if the fields are used with a "= ?" placeholder.
     * @param isCondition boolean | "true" if the fields are used as conditions.
     * @param isUpdate boolean | "true" if the fields are used for an update.
     * @return String | Formatted fields used for a query.
     */
    private String generateQueryFields(Set<String> fields, boolean isUnknownParameter, boolean isCondition, boolean isUpdate) {
        StringBuilder fieldsSelection = new StringBuilder();
        String separator = "";
        for (String field : fields) {
            if (this.identifiers.contains(field) && isUpdate) { continue; }
            fieldsSelection.append(separator).append(field);
            if (isUnknownParameter) { fieldsSelection.append(" = ?"); }
            separator = isCondition ? " AND " : ", ";
        }
        return fieldsSelection.toString();
    }

    private String generateQueryFields() {
        return generateQueryFields(this.fields.keySet(), false, false, false); // Default values.
    }

    private Map<String, Object> generateIdentifiersMap(int... identifiers) {
        Map<String, Object> fields = new HashMap<String, Object>();
        int index = 0;
        for (int identifier : identifiers) {
            fields.put(this.identifiers.get(index), identifier);
            index ++;
        }
        return fields;
    }
}
