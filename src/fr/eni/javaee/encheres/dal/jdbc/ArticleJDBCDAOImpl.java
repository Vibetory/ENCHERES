package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ArticleJDBCDAOImpl extends GenericJDBCDAOImpl<Article> {
    public ArticleJDBCDAOImpl() throws EException {
        super();
    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("noArticle");
    }

    @Override
    protected void setFields() {
        this.fields = new HashMap<String, String>() {{
            put("noArticle", "int");
            put("nomArticle", "String");
            put("description", "String");
            put("dateDebutEncheres", "Date");
            put("dateFinEncheres", "Date");
            put("miseAPrix", "int");
            put("prixVente", "int");
            put("vendeur", "int");
            put("acquereur", "int");
            put("categorie", "int");
        }};
    }

    @Override
    protected Article getObject() {
        return null;
    }

    @Override
    protected Article generateObject(ResultSet resultSet) {
        return null;
    }
}
