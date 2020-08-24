package fr.eni.javaee.encheres.bo;

public class Categorie {
    private int noCategorie;
    private String libelle;

    // CONSTRUCTORS

    public Categorie(int noCategorie, String libelle) {
        this(libelle);
        setNoCategorie(noCategorie);
    }

    public Categorie(String libelle) { setLibelle(libelle); }


    // METHODS
    public static String getIdentifierName() { return "no_categorie"; }


    // GETTERS AND SETTERS

    public int getNoCategorie() { return this.noCategorie;}

    public void setNoCategorie(int noCategorie) { this.noCategorie = noCategorie; }

    public String getLibelle() { return this.libelle; }

    public void setLibelle(String libelle) { this.libelle = libelle; }
}
