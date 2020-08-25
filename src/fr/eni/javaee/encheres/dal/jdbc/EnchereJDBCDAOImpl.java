package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Enchere;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class EnchereJDBCDAOImpl extends GenericJDBCDAOImpl<Enchere> {
    public EnchereJDBCDAOImpl() throws EException {
        super();
    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("articleVendu");
        this.identifiers.add("encherisseur");
    }

    @Override
    protected void setFields() {
        this.fields = new LinkedHashMap<String, String>() {{
            put("articleVendu", "fr.eni.javaee.encheres.dal.jdbc.ArticleJDBCDAOImpl");
            put("encherisseur", "fr.eni.javaee.encheres.dal.jdbc.UtilisateurJDBCDAOImpl");
            put("dateEnchère", "Date");
            put("montantEnchere", "int");
        }};
    }

    @Override
    protected Enchere getObject() { return new Enchere(); }
}
