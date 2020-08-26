package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Utilisateur;

import java.time.LocalDate;
import java.util.List;

public class ArticleManager extends GenericManager<Article> {

    public ArticleManager() throws EException {
        super();
    }

    @Override
    public Article getById(int... identifiers) throws EException {
        Article article =  super.getById(identifiers);
        article.setEtatVente();
        return article;
    }

    private List<Article> getArticlesFrom(String field, Utilisateur utilisateur) throws EException {
        int noUtilisateur = utilisateur.getNoUtilisateur();
        try { return this.DAOBusinessObject.selectAllByField(field, noUtilisateur); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.ARTICLE_GET_ALL_FROM_ERROR, eException);
        }
    }

    public List<Article> getArticlesFromVendeur(Utilisateur vendeur) throws EException {
        return getArticlesFrom("vendeur", vendeur);
    }

    public List<Article> getArticlesFromAcquereur(Utilisateur acquereur) throws EException {
        return getArticlesFrom("acquereur", acquereur);
    }

    @Override
    protected void executeLogic(Article object, boolean update) throws EException {
        try {
            checkAttributes(object);
        } catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.UTILISATEUR_ADD_CHECK_ERROR, eException);
        }
    }

    private void checkAttributes(Article article) throws EException {
        if (article == null) { throw new EException(CodesExceptionBLL.BO_NULL_ERROR.get("Article")); }
        StringBuilder errors = new StringBuilder();
        if (article.getNomArticle() == null || article.getNomArticle() .isEmpty()) {
            errors.append("Champs obligatoire. L'article n'a pas de nom.").append("\n");
        }
        if (article.getDescription() == null || article.getDescription().isEmpty()) {
            errors.append("Champs obligatoire. L'article n'a pas de description.").append("\n");
        }
        if (article.getDateDebutEncheres() == null) { article.setDateDebutEncheres(LocalDate.now()); }
        if (article.getDateFinEncheres() == null) {
            errors.append("Champs obligatoire. L'article n'a pas de date de fin d'enchère.").append("\n");
        }
        if (article.getDateFinEncheres().isBefore(LocalDate.now()) || article.getDateFinEncheres().isBefore(article.getDateDebutEncheres())) {
            errors.append("Champs incorrecte. La date de fin d'enchère est invalide.").append("\n");
        }
        if (article.getVendeur() == null) {
            errors.append("Champs obligatoire. L'article n'a pas de vendeur.").append("\n");
        }
        if (article.getCategorie() == null) {
            errors.append("Champs obligatoire. L'article n'a pas de catégorie.").append("\n");
        }
        if (article.getMiseAPrix() < 0) {
            errors.append("Champs incorrecte. La mise à prix ne peut pas être négative.").append("\n");
        }
        if (article.getPrixVente() < 0) {
            errors.append("Champs incorrecte. Le prix de vente ne peut pas être négatif").append("\n");
        }
        if (!errors.toString().isEmpty()) { throw new EException(errors.toString()); }
    }

}
