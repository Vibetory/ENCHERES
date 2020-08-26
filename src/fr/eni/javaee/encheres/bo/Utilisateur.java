package fr.eni.javaee.encheres.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilisateur {
    private int noUtilisateur, credit;
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
        setCredit(0);
        setAdministrateur((byte) 0);
    }

    public Utilisateur(String pseudo, String nom, String prenom, String email, String rue, String codePostal, String ville, String motDePasse, int credit, byte administrateur) {
        this(pseudo, nom, prenom, email, rue, codePostal, ville, motDePasse);
        setCredit(credit);
        setAdministrateur(administrateur);
    }

    public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse, int credit, byte administrateur) {
        this(pseudo, nom, prenom, email, rue, codePostal, ville, motDePasse, credit, administrateur);
        setTelephone(telephone);
    }

    public Utilisateur(int noUtilisateur, String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse, int credit, byte administrateur) {
        this(pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur);
        setNoUtilisateur(noUtilisateur);
    }

    // GETTERS & SETTERS

    public int getNoUtilisateur() { return this.noUtilisateur; }

    public void setNoUtilisateur(int noUtilisateur) { this.noUtilisateur = noUtilisateur; }

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
