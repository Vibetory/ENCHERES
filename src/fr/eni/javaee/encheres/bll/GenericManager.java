package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericManager<T> {
    protected Class<T> entityClass;
    protected DAO DAOBusinessObject;

    public GenericManager() throws EException {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try { DAOBusinessObject = DAOFactory.getBusinessObjectDAO(this.getActualClassName()); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.INITIALIZATION_DAO_ERROR.get(this.getActualClassName()), eException);
        }
    }

    public List<T> getAll() throws EException {
        try { return DAOBusinessObject.selectAll(); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.GET_ALL_ERROR.get(this.getActualClassName()), eException);
        }
    }

    public T getById(int... identifiers) throws EException {
        try { return (T) DAOBusinessObject.selectById(identifiers); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.GET_BY_ID_ERROR.get(this.getActualClassName()), eException);
        }
    }

    public T add(T object) throws EException {
        doChecks(object, false);
        try { return (T) DAOBusinessObject.insert(object); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.ADD_ERROR.get(this.getActualClassName()), eException);
        }
    }

    public T update(T object) throws EException {
        doChecks(object, true);
        try { return (T) DAOBusinessObject.update(object); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.ADD_ERROR.get(this.getActualClassName()), eException);
        }
    }

    protected abstract void doChecks(T object, boolean update) throws EException;

    private String getActualClassName() { return this.entityClass.getSimpleName(); }
}
