package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Categorie;

import java.sql.ResultSet;
import java.util.ArrayList;

public class CategorieJDBCDAOImpl extends GenericJDBCDAOImpl<Categorie> {
    public CategorieJDBCDAOImpl() throws EException {
        super();
    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("noCategorie");
    }

    @Override
    protected void setFields() {

    }

    @Override
    protected Categorie getObject() {
        return null;
    }

    @Override
    protected Categorie generateObject(ResultSet resultSet) {
        return null;
    }
}
