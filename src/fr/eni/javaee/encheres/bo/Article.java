package fr.eni.javaee.encheres.bo;

import java.time.LocalDate;
import java.util.*;

public class Article {
    private static final String[] etatsVente = {"Créée", "En cours", "Enchères terminées", "Retrait effectué"};
    private static Map<Integer, Article> articlesVendus = new HashMap<>();
    private Map<Utilisateur, Enchere> encheres;
    private int noArticle, miseAPrix, prixVente;
    private String nomArticle, description, etatVente;
    private LocalDate dateDebutEncheres, dateFinEncheres;
    private Categorie categorie;
    private Utilisateur vendeur, acquereur;
    private boolean retirer;


    // CONSTRUCTORS

    public Article(String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, Categorie categorie, Utilisateur vendeur) {
        setNomArticle(nomArticle);
        setDescription(description);
        setDateDebutEncheres(dateDebutEncheres == null ? LocalDate.now() : dateDebutEncheres);
        setDateFinEncheres(dateFinEncheres);
        setCategorie(categorie);
        setVendeur(vendeur);
        setEtatVente();
        setEnchères();
    }

    public Article(String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, Categorie categorie, Utilisateur vendeur, int miseAPrix) {
        this(nomArticle, description, dateDebutEncheres, dateFinEncheres, categorie, vendeur);
        setMiseAPrix(miseAPrix);
    }

    public Article(int noArticle, String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, Categorie categorie, Utilisateur vendeur, int miseAPrix) {
        this(nomArticle, description, dateDebutEncheres, dateFinEncheres, categorie, vendeur, miseAPrix);
        setNoArticle(noArticle);
    }

    public Article (int noArticle, String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, Categorie categorie, Utilisateur vendeur, int miseAPrix, int prixVente) {
        this(noArticle, nomArticle, description, dateDebutEncheres, dateFinEncheres, categorie, vendeur, miseAPrix);
        setPrixVente(prixVente);
    }

    public Article (int noArticle, String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, Categorie categorie, Utilisateur vendeur, int miseAPrix, int prixVente, Utilisateur acquereur) {
        this(noArticle, nomArticle, description, dateDebutEncheres, dateFinEncheres, categorie, vendeur, miseAPrix);
        setAcquereur(acquereur);
    }


    // METHODS

    /**
     * Get the highest auction.
     * @return Enchere | Highest auction.
     */
    public Enchere getMaxEnchere() {
        Enchere max = new Enchere();
        max.setMontantEnchere(Integer.MIN_VALUE);
        for (Map.Entry<Utilisateur, Enchere> enchere : encheres.entrySet()) {
            max = enchere.getValue().getMontantEnchere() > max.getMontantEnchere() ? enchere.getValue() : max;
        }
        return max;
    }

    /**
     * Add an auction on the article.
     * @param encherisseur Utilisateur | User doing an auction.
     * @param montant int | Amount in credits.
     */
    public void addEnchere(Utilisateur encherisseur, int montant) {
        Enchere enchere = new Enchere(this, encherisseur, montant);
        this.encheres.put(encherisseur, enchere);
        encherisseur.getEncheres().put(this, enchere);
    }


    // GETTERS & SETTERS

    public static Map<Integer, Article> getArticlesVendus() { return articlesVendus; }

    public static Article getArticleVendu(int noArticle) { return articlesVendus.get(noArticle); }

    public Map<Utilisateur, Enchere> getEncheres() { return this.encheres; }

    public Enchere getEnchere(Utilisateur encherisseur) { return this.encheres.get(encherisseur); }

    public void setEnchères() { this.encheres = new HashMap<>(); }

    public int getNoArticle() { return this.noArticle; }

    /**
     * Set an identifier for an article.
     * Add the user to the map of articles.
     * @param noArticle int | Identifier.
     */
    public void setNoArticle(int noArticle) {
        this.noArticle = noArticle;
        articlesVendus.put(noArticle, this);
    }

    public int getMiseAPrix() { return this.miseAPrix; }

    public void setMiseAPrix(int miseAPrix) { this.miseAPrix = miseAPrix; }

    public int getPrixVente() { return this.prixVente; }

    public void setPrixVente(int prixVente) { this.prixVente = prixVente; }

    public String getNomArticle() { return this.nomArticle; }

    public void setNomArticle(String nomArticle) { this.nomArticle = nomArticle; }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

    public String getEtatVente() {
        setEtatVente();
        return this.etatVente;
    }

    public void setEtatVente() {
        if (this.dateDebutEncheres.isAfter(LocalDate.now())) { this.etatVente = etatsVente[0]; }
        else if (this.dateFinEncheres.isBefore(LocalDate.now())) {
            this.etatVente = this.retirer ? etatsVente[3] : etatsVente[2];
        } else { this.etatVente = etatsVente[1]; }
    }

    public LocalDate getDateDebutEncheres() { return this.dateDebutEncheres; }

    public void setDateDebutEncheres(LocalDate dateDebutEncheres) { this.dateDebutEncheres = dateDebutEncheres; }

    public LocalDate getDateFinEncheres() { return this.dateFinEncheres; }

    public void setDateFinEncheres(LocalDate dateFinEncheres) { this.dateFinEncheres = dateFinEncheres; }

    public Categorie getCategorie() { return categorie; }

    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public Utilisateur getVendeur() { return this.vendeur; }

    public void setVendeur(Utilisateur vendeur) {
        this.vendeur = vendeur;
        vendeur.addArticleVendu(this);
    }

    public Utilisateur getAcquereur() { return this.acquereur; }

    public void setAcquereur(Utilisateur acquereur) {
        this.acquereur = acquereur;
        acquereur.addArticleAcquis(this);
    }

    public boolean isRetirer() { return this.retirer; }

    public void setRetirer(boolean retirer) { this.retirer = retirer; }
}
