package fr.eni.javaee.encheres.bo;

import java.time.LocalDateTime;

public class Enchere {
    private Article articleVendu;
    private Utilisateur encherisseur;
    private LocalDateTime dateEnchere;
    private int montantEnchere;

    // CONSTRUCTORS

    public Enchere() {};

    public Enchere(Article articleVendu, Utilisateur encherisseur, int montantEnchere) {
        setArticleVendu(articleVendu);
        setEncherisseur(encherisseur);
        setDateEnchere(LocalDateTime.now());
        setMontantEnchere(montantEnchere);
    }

    public Enchere(Article articleVendu, Utilisateur encherisseur, LocalDateTime dateEnchere, int montantEnchere) {
        this(articleVendu, encherisseur, montantEnchere);
        setDateEnchere(dateEnchere);
    }


    // GETTERS & SETTERS

    public Article getArticleVendu() { return this.articleVendu; }

    public int getNoArticleVendu() {
        return this.articleVendu != null ? this.articleVendu.getNoArticle() : 0;
    }

    public void setArticleVendu(Article articleVendu) { this.articleVendu = articleVendu; }

    public Utilisateur getEncherisseur() { return this.encherisseur; }

    public int getNoEncherisseur() {
        return this.encherisseur != null ? this.encherisseur.getNoUtilisateur() : 0;
    }

    public void setEncherisseur(Utilisateur encherisseur) { this.encherisseur = encherisseur; }

    public LocalDateTime getDateEnchere() { return this.dateEnchere; }

    public void setDateEnchere(LocalDateTime dateEnchere) { this.dateEnchere = dateEnchere; }

    public int getMontantEnchere() { return this.montantEnchere; }

    public void setMontantEnchere(int montantEnchere) { this.montantEnchere = montantEnchere; }
}
