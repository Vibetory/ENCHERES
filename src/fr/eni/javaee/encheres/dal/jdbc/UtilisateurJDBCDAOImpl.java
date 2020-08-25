package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Utilisateur;

import java.util.*;

public class UtilisateurJDBCDAOImpl extends GenericJDBCDAOImpl<Utilisateur> {

    public UtilisateurJDBCDAOImpl() throws EException {
        super();
    }

    @Override
    protected void setIdentifiers() {
        this.identifiers = new ArrayList<>();
        this.identifiers.add("no_utilisateur");
    }

    @Override
    protected void setFields() {
        this.fields = new LinkedHashMap<String, String>() {{
            put("noUtilisateur", "int");
            put("pseudo", "String");
            put("nom", "String");
            put("prenom", "String");
            put("email", "String");
            put("telephone", "String");
            put("rue", "String");
            put("codePostal", "String");
            put("ville", "String");
            put("motDePasse", "String");
            put("credit", "int");
            put("administrateur", "byte");
        }};
    }

    protected Utilisateur getObject() {
        return new Utilisateur();
    }

    @Override
    protected void setSetters() {
        this.setters = new ArrayList<>();
        try {
            this.setters.add(Utilisateur.class.getMethod("setNoUtilisateur", int.class));
            this.setters.add(Utilisateur.class.getMethod("setPseudo", String.class));
            this.setters.add(Utilisateur.class.getMethod("setNom", String.class));
            this.setters.add(Utilisateur.class.getMethod("setPrenom", String.class));
            this.setters.add(Utilisateur.class.getMethod("setEmail", String.class));
            this.setters.add(Utilisateur.class.getMethod("setTelephone", String.class));
            this.setters.add(Utilisateur.class.getMethod("setRue", String.class));
            this.setters.add(Utilisateur.class.getMethod("setCodePostal", String.class));
            this.setters.add(Utilisateur.class.getMethod("setVille", String.class));
            this.setters.add(Utilisateur.class.getMethod("setMotDePasse", String.class));
            this.setters.add(Utilisateur.class.getMethod("setCredit", int.class));
            this.setters.add(Utilisateur.class.getMethod("setAdministrateur", byte.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}