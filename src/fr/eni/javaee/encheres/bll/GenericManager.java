package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;

public abstract class GenericManager<T> {
    protected Class<T> entityClass;
    protected DAO DAOBusinessObject;

    public GenericManager() throws EException {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try { DAOBusinessObject = DAOFactory.getBusinessObjectDAO(this.getActualClassName()); }
        catch (EException eException) {
            throw new EException(CodesExceptionBLL.INITIALIZATION_DAO_ERROR.get(this.getActualClassName()), eException);
        }
    }

    public List<T> getAll() throws EException {
        try { return DAOBusinessObject.selectAll(); }
        catch (EException eException) {
            throw new EException(CodesExceptionBLL.GET_ALL_ERROR.get(this.getActualClassName()), eException);
        }
    }

    public T getById(int... identifiers) throws EException {
        try { return (T) DAOBusinessObject.selectById(identifiers); }
        catch (EException eException) {
            throw new EException(CodesExceptionBLL.GET_BY_ID_ERROR.get(this.getActualClassName()), eException);
        }
    }

    public T add(T object) throws EException {
        try {
            boolean alreadyExists = checkUnicity(object);
            if (alreadyExists) {
                throw new EException(CodesExceptionBLL.ADD_ALREADY_EXIST_ERROR.get(getActualClassName()));
            }
            checkAttributes(object);
            object = (T) DAOBusinessObject.insert(object);
            executeUpdate(object, "INSERT");
            return object;
        }
        catch (EException eException) {
            throw new EException(CodesExceptionBLL.ADD_ERROR.get(this.getActualClassName()), eException);
        }
    }

    public T update(T object) throws EException {
        try {
            boolean alreadyExists = checkUnicity(object);
            if (!alreadyExists) {
                throw new EException(CodesExceptionBLL.UPDATE_NOT_EXIST_ERROR.get(getActualClassName()));
            }
            checkAttributes(object);
            object = (T) DAOBusinessObject.update(object);
            executeUpdate(object, "UPDATE");
            return object;
        }
        catch (EException eException) {
            throw new EException(CodesExceptionBLL.UPDATE_ERROR.get(this.getActualClassName()), eException);
        }
    }

    public void delete(T object) throws EException {
        try {
            boolean alreadyExists = checkUnicity(object);
            if (!alreadyExists) {
                throw new EException(CodesExceptionBLL.DELETE_NOT_EXIST_ERROR.get(getActualClassName()));
            }
            int[] identifiers = getIdentifiers(object);
            DAOBusinessObject.delete(identifiers);
            executeUpdate(object, "DELETE");
        }
        catch (EException eException) {
            throw new EException(CodesExceptionBLL.UPDATE_ERROR.get(this.getActualClassName()), eException);
        }
    }


    protected abstract int[] getIdentifiers(T object);

    protected abstract void executeUpdate(T object, String operationCRUD) throws EException;
    protected abstract boolean checkUnicity(T Object) throws EException;
    protected abstract void checkAttributes(T Object) throws EException;


    private String getActualClassName() { return this.entityClass.getSimpleName(); }
}
