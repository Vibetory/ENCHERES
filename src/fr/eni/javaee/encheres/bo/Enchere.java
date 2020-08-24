package fr.eni.javaee.encheres.bo;

import java.time.LocalDate;

public class Enchere {
    private Article articleVendu;
    private Utilisateur encherisseur;
    private LocalDate dateEnchère;
    private int montantEnchere;

    // CONSTRUCTORS

    public Enchere() {};

    public Enchere(Article article, Utilisateur encherisseur, int montantEnchere) {
        setArticleVendu(article);
        setEncherisseur(encherisseur);
        setDateEnchère(LocalDate.now());
        setMontantEnchere(montantEnchere);
    }

    public Enchere(Article article, Utilisateur encherisseur, int montantEnchere, LocalDate dateEnchère) {
        this(article,encherisseur, montantEnchere);
        setDateEnchère(dateEnchère);
    }


    // METHODS
    public static String getIdentifierName() { return "no_article"; }


    // GETTERS & SETTERS

    public Article getArticleVendu() { return this.articleVendu; }

    public void setArticleVendu(Article articleVendu) { this.articleVendu = articleVendu; }

    public Utilisateur getEncherisseur() { return encherisseur; }

    public void setEncherisseur(Utilisateur encherisseur) { this.encherisseur = encherisseur; }

    public LocalDate getDateEnchère() { return this.dateEnchère; }

    public void setDateEnchère(LocalDate dateEnchère) { this.dateEnchère = dateEnchère; }

    public int getMontantEnchere() { return this.montantEnchere; }

    public void setMontantEnchere(int montantEnchere) { this.montantEnchere = montantEnchere; }
}
