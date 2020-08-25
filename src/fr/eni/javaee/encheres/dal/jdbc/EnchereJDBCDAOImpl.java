package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Enchere;

import java.sql.ResultSet;
import java.util.ArrayList;

public class EnchereJDBCDAOImpl extends GenericJDBCDAOImpl<Enchere> {
    public EnchereJDBCDAOImpl() throws EException {
        super();
    }

    @Override
    protected void setSetters() {

    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("no_article");
        this.identifiers.add("no_utilisateur");
    }

    @Override
    protected void setFields() {

    }

    @Override
    protected Enchere getObject() {
        return null;
    }

    @Override
    protected Enchere generateObject(ResultSet resultSet) {
        return null;
    }
}
