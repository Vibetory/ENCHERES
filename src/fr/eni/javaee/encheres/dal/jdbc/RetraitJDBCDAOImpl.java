package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Retrait;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RetraitJDBCDAOImpl extends GenericJDBCDAOImpl<Retrait> {
    public RetraitJDBCDAOImpl() throws EException {
        super();
    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("articleARetirer");
    }

    @Override
    protected void setFields() {
        this.fields = new LinkedHashMap<String, String>() {{
            put("articleARetirer", "int");
            put("rue", "String");
            put("codePostal", "String");
            put("ville", "String");
        }};
    }

    @Override
    protected Retrait getObject() { return new Retrait(); }
}
