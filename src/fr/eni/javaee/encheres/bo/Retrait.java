package fr.eni.javaee.encheres.bo;

public class Retrait {
    private String rue, codePostal, ville;
    private Article articleARetirer;

    // CONSTRUCTORS
    public Retrait() {}

    public Retrait(Article articleARetirer) {
        setArticleARetirer(articleARetirer);
        setRue(articleARetirer.getVendeur().getRue());
        setCodePostal(articleARetirer.getVendeur().getCodePostal());
        setVille(articleARetirer.getVendeur().getVille());
    }


    public Retrait(Article articleARetirer, String rue, String codePostal, String ville) {
        setArticleARetirer(articleARetirer);
        setRue(rue);
        setCodePostal(codePostal);
        setVille(ville);
    }


    // METHODS
    public static String getIdentifierName() { return "no_article"; }


    // GETTERS & SETTERS

    public String getRue() { return this.rue; }

    public void setRue(String rue) { this.rue = rue; }

    public String getCodePostal() { return this.codePostal; }

    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getVille() { return ville; }

    public void setVille(String ville) { this.ville = ville; }

    public Article getArticleARetirer() { return this.articleARetirer; }

    public int getNoArticleARetirer() {
        return this.articleARetirer != null ? this.articleARetirer.getNoArticle() : 0;
    }

    public void setArticleARetirer(Article articleARetirer) { this.articleARetirer = articleARetirer; }
}
