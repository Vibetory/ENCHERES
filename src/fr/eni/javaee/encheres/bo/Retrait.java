package fr.eni.javaee.encheres.bo;

import java.util.ArrayList;
import java.util.List;

public class Retrait {
    List<Article> articlesARetirer;
    private String rue, codePostal, ville;

    // CONSTRUCTORS

    public Retrait(String rue, String codePostal, String ville) {
        setRue(rue);
        setCodePostal(codePostal);
        setVille(ville);
        setArticlesARetirer();
    }


    // METHODS

    /**
     * Set an article as having not been withdrawn by the acquirer and add it to the list.
     */
    public void addArticle(Article articleARetirer) {
        articleARetirer.setRetirer(false);
        this.articlesARetirer.add(articleARetirer);
    }

    /**
     * Set an article as having been withdrawn by the acquirer and remove it from the list.
     */
    public void removeArticle(Article articleARetirer) {
        articleARetirer.setRetirer(true);
        this.articlesARetirer.remove(articleARetirer);
    }


    // GETTERS & SETTERS

    public List<Article> getarticlesARetirer() { return this.articlesARetirer; }

    public void setArticlesARetirer() { this.articlesARetirer = new ArrayList<>(); }

    public String getRue() { return this.rue; }

    public void setRue(String rue) { this.rue = rue; }

    public String getCodePostal() { return this.codePostal; }

    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getVille() { return ville; }

    public void setVille(String ville) { this.ville = ville; }



}
