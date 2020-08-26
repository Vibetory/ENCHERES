package fr.eni.javaee.encheres.bo;

import java.time.LocalDate;
import java.util.Collection;

public class Enchere {
    private Article articleVendu;
    private Utilisateur encherisseur;
    private LocalDate dateEnchère;
    private int montantEnchere;

    // CONSTRUCTORS

    public Enchere() {};

    public Enchere(Article articleVendu, Utilisateur encherisseur, int montantEnchere) {
        setArticleVendu(articleVendu);
        setEncherisseur(encherisseur);
        setDateEnchère(LocalDate.now());
        setMontantEnchere(montantEnchere);
    }

    public Enchere(Article articleVendu, Utilisateur encherisseur, LocalDate dateEnchère, int montantEnchere) {
        this(articleVendu, encherisseur, montantEnchere);
        setDateEnchère(dateEnchère);
    }


    // GETTERS & SETTERS

    public Article getArticleVendu() { return this.articleVendu; }

    public int getNoArticleVendu() { return this.articleVendu.getNoArticle(); }

    public void setArticleVendu(Article articleVendu) { this.articleVendu = articleVendu; }

    public Utilisateur getEncherisseur() { return this.encherisseur; }

    public int getNoEncherisseur() { return this.encherisseur.getNoUtilisateur(); }

    public void setEncherisseur(Utilisateur encherisseur) { this.encherisseur = encherisseur; }

    public LocalDate getDateEnchère() { return this.dateEnchère; }

    public void setDateEnchère(LocalDate dateEnchère) { this.dateEnchère = dateEnchère; }

    public int getMontantEnchere() { return this.montantEnchere; }

    public void setMontantEnchere(int montantEnchere) { this.montantEnchere = montantEnchere; }
}
