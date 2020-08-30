package fr.eni.javaee.encheres.bo;

public class Utilisateur {
    private int noUtilisateur, credits;
    private String pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse;
    private byte administrateur;

    // CONSTRUCTORS

    public Utilisateur() {}

    public Utilisateur(String pseudo, String nom, String prenom, String email, String rue, String codePostal, String ville, String motDePasse) {
        setPseudo(pseudo);
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setRue(rue);
        setCodePostal(codePostal);
        setVille(ville);
        setMotDePasse(motDePasse);
        setCredits(500);
        setAdministrateur((byte) 0);
    }

    public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse) {
        this(pseudo, nom, prenom, email, rue, codePostal, ville, motDePasse);
        setTelephone(telephone);
    }

    public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse, int credits) {
        this(pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse);
        setCredits(credits);
    }

    public Utilisateur(String pseudo, String nom, String prenom, String email, String rue, String codePostal, String ville, String motDePasse, int credits, byte administrateur) {
        this(pseudo, nom, prenom, email, rue, codePostal, ville, motDePasse);
        setCredits(credits);
        setAdministrateur(administrateur);
    }

    public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse, int credits, byte administrateur) {
        this(pseudo, nom, prenom, email, rue, codePostal, ville, motDePasse, credits, administrateur);
        setTelephone(telephone);
    }

    public Utilisateur(int noUtilisateur, String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse, int credits, byte administrateur) {
        this(pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credits, administrateur);
        setNoUtilisateur(noUtilisateur);
    }

    // METHODS

    public void addCredits(int credits) { this.credits += credits; }
    public void substractCredits(int credits) { this.credits -= credits; }

    // GETTERS & SETTERS

    public int getNoUtilisateur() { return this.noUtilisateur; }

    public void setNoUtilisateur(int noUtilisateur) { this.noUtilisateur = noUtilisateur; }

    public int getCredits() { return this.credits; }

    public void setCredits(int credits) { this.credits = credits;}

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
