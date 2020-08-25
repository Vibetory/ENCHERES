package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Retrait;

import java.sql.ResultSet;
import java.util.ArrayList;

public class RetraitJDBCDAOImpl extends GenericJDBCDAOImpl<Retrait> {
    public RetraitJDBCDAOImpl() throws EException {
        super();
    }

    @Override
    protected void setSetters() {

    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("no_article");
    }

    @Override
    protected void setFields() {

    }

    @Override
    protected Retrait getObject() {
        return null;
    }

    @Override
    protected Retrait generateObject(ResultSet resultSet) {
        return null;
    }
}
