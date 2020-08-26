package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Categorie;
;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CategorieJDBCDAOImpl extends GenericJDBCDAOImpl<Categorie> {
    public CategorieJDBCDAOImpl() throws EException {
        super();
    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("noCategorie");
        this.autoIdentifiers = 1;
    }

    @Override
    protected void setFields() {
        this.fields = new LinkedHashMap<String, String>() {{
            put("noCategorie", "int");
            put("libelle", "String");
        }};
    }

    @Override
    protected Categorie getObject() { return new Categorie(); }
}
