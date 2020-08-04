package fr.eni.javaee.encheres.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilisateur {
    private static Map<Integer, Utilisateur> utilisateurs = new HashMap<>();
    private Map<Article, Enchere> encheres;
    private List<Article> articlesVendus, articlesAcquis;
    private int noUtilisateur, credit;
    private String pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse;
    private byte administrateur;

    // CONSTRUCTORS

    public Utilisateur(String pseudo, String nom, String prenom, String email, String rue, String codePostal, String ville, String motDePasse, int credit, byte administrateur) {
        setPseudo(pseudo);
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setRue(rue);
        setCodePostal(codePostal);
        setVille(ville);
        setMotDePasse(motDePasse);
        setCredit(credit);
        setAdministrateur(administrateur);
        setArticlesVendus();
        setArticlesAcquis();
        setEnchères();
    }

    // CONSTRUCTORS

    public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse, int credit, byte administrateur) {
        this(pseudo, nom, prenom, email, rue, codePostal, ville, motDePasse, credit, administrateur);
        setTelephone(telephone);
    }

    public Utilisateur(int noUtilisateur, String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse, int credit, byte administrateur) {
        this(pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur);
        setNoUtilisateur(noUtilisateur);
    }


    // METHODS

    /**
     * Add an article to a vendor.
     * @param article ArticleVendu | Article to add.
     */
    public void addArticleVendu(Article article) { this.articlesVendus.add(article); }

    /**
     * Remove an article to a vendor.
     * @param article ArticleVendu | Article to remove.
     */
    public void removeArticleVendu(Article article) { this.articlesVendus.remove(article); }

    /**
     * Add an article to the acquirer.
     * @param articleAcquis ArticleVendu | Article to add.
     */
    public void addArticleAcquis(Article articleAcquis) { this.articlesVendus.add(articleAcquis); }

    /**
     * Add an auction on an article.
     * @param article Article | Article.
     * @param montant int | Amount in credits.
     */
    public void addEnchere(Article article, int montant) { article.addEnchere(this, montant); }


    // GETTERS & SETTERS

    public static Map<Integer, Utilisateur> getUtilisateurs() { return utilisateurs; }

    public static Utilisateur getUtilisateur(int noUtilisateur) { return utilisateurs.get(noUtilisateur); }

    public void setArticlesVendus() { this.articlesVendus = new ArrayList<>(); }

    public List<Article> getArticlesVendus() { return this.articlesVendus; }

    public void setArticlesAcquis() { this.articlesAcquis = new ArrayList<>(); }

    public List<Article> getArticlesAcquis() { return this.articlesAcquis; }

    public int getNoUtilisateur() { return this.noUtilisateur; }

    public Map<Article, Enchere> getEncheres() { return this.encheres; }

    public Enchere getEnchere(Article articleVendu) { return this.encheres.get(articleVendu); }

    public void setEnchères() { this.encheres = new HashMap<>(); }

    /**
     * Set an identifier for an user.
     * Add the user to the map of users.
     * @param noUtilisateur int | Identifier.
     */
    public void setNoUtilisateur(int noUtilisateur) {
        this.noUtilisateur = noUtilisateur;
        utilisateurs.put(noUtilisateur, this);
    }

    public int getCredit() { return this.credit; }

    public void setCredit(int credit) { this.credit = credit;}

    public String getPseudo() { return this.pseudo; }

    public void setPseudo(String pseudo) { this.pseudo = pseudo; }

    public String getNom() { return this.nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return this.prenom; }

    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return this.telephone; }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getRue() { return this.rue; }

    public void setRue(String rue) { this.rue = rue; }

    public String getCodePostal() { return this.codePostal; }

    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getVille() { return ville; }

    public void setVille(String ville) { this.ville = ville; }

    public String getMotDePasse() { return this.motDePasse; }

    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public byte getAdministrateur() { return administrateur; }

    public void setAdministrateur(byte administrateur) { this.administrateur = administrateur; }
}
