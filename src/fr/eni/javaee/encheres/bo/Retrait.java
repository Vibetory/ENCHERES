package fr.eni.javaee.encheres.bo;

import java.util.ArrayList;
import java.util.List;

public class Retrait {
    private String rue, codePostal, ville;

    // CONSTRUCTORS

    public Retrait(String rue, String codePostal, String ville) {
        setRue(rue);
        setCodePostal(codePostal);
        setVille(ville);
    }


    // GETTERS & SETTERS

    public String getRue() { return this.rue; }

    public void setRue(String rue) { this.rue = rue; }

    public String getCodePostal() { return this.codePostal; }

    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getVille() { return ville; }

    public void setVille(String ville) { this.ville = ville; }
}
