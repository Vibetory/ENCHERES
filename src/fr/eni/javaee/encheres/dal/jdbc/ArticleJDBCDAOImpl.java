package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ArticleJDBCDAOImpl extends GenericJDBCDAOImpl<Article> {
    public ArticleJDBCDAOImpl() throws EException {
        super();
    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("noArticle");
        this.autoIdentifiers = 1;
    }

    @Override
    protected void setFields() {
        this.fields = new LinkedHashMap<String, String>() {{
            put("noArticle", "int");
            put("nomArticle", "String");
            put("description", "String");
            put("dateDebutEncheres", "Date");
            put("dateFinEncheres", "Date");
            put("miseAPrix", "int");
            put("prixVente", "int");
            put("vendeur", "fr.eni.javaee.encheres.dal.jdbc.UtilisateurJDBCDAOImpl");
            put("acquereur", "fr.eni.javaee.encheres.dal.jdbc.UtilisateurJDBCDAOImpl");
            put("categorie", "fr.eni.javaee.encheres.dal.jdbc.CategorieJDBCDAOImpl");
        }};
    }

    @Override
    protected Article getObject() { return new Article(); }
}
