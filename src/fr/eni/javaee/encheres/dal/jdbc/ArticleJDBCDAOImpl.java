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
    protected void setSetters() {

    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("no_article");
    }

    @Override
    protected void setFields() {
        this.fields = new HashMap<String, String>() {{
            put("no_article", "int");
            put("nom_article", "String");
            put("description", "String");
            put("date_debut_encheres", "Date");
            put("date_fin_encheres", "Date");
            put("prix_initial", "int");
            put("prix_vente", "int");
            put("no_vendeur", "int");
            put("no_acquereur", "int");
            put("no_categorie", "int");
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
