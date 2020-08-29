package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.jdbc.TransactSQLQueries;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleManager extends GenericManager<Article> {
    private final DAO<Article> DAOArticle;

    public ArticleManager() throws EException {
        super();
        this.DAOArticle = DAOFactory.getArticleDAO();
    }

    @Override
    public Article getById(int... identifiers) throws EException {
        Article article =  super.getById(identifiers);
        article.setEtatVente();
        return article;
    }

    private List<Article> getArticlesFrom(String field, Utilisateur utilisateur) throws EException {
        int noUtilisateur = utilisateur.getNoUtilisateur();
        try { return this.DAOArticle.selectAllByField(field, noUtilisateur); }
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

    public List<Article> getArticlesByNomOrDescription(String variable) throws EException {
        return DAOArticle.selectAllBy(TransactSQLQueries.SELECT_ARTICLES_LIKE(variable), null);
    }

    /**
     * @param articles List<Article> | List of articles to filter by categorie.
     * @param etatVente String | etatVente to apply the filter on.
     * @return List<Article> | List of filtered articles.
     * @throws EException EException
     */
    public List<Article> filterByEtat(List<Article> articles, String etatVente) throws EException {
        return articles
                .stream()
                .filter(article -> article.getEtatVente().equals(etatVente))
                .collect(Collectors.toList());
    }

    /**
     * @param articles List<Article> | List of articles to filter by categorie.
     * @param categorie String | Categorie to apply the filter on.
     * @return List<Article> | List of filtered articles.
     * @throws EException EException
     */
    public List<Article> filteredByCategorie(List<Article> articles,  String categorie) throws EException {
        return articles
                .stream()
                .filter(article -> article.getCategorie().getLibelle().equals(categorie))
                .collect(Collectors.toList());
    }

    @Override
    protected int[] executeLogic(Article article, String operationCRUD) throws EException {
        if (operationCRUD.equals("UPDATE") && !checkUnicity(article)) { throw new EException(CodesExceptionBLL.UPDATE_NOT_EXIST_ERROR.get("Article")); }
        if (operationCRUD.equals("DELETE") && !checkUnicity(article)) { throw new EException(CodesExceptionBLL.DELETE_NOT_EXIST_ERROR.get("Article")); }
        else { checkAttributes(article); }
        return operationCRUD.equals("INSERT") ? null : new int[] {article.getNoArticle()};
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
        if (article.getDateDebutEncheres() == null) { article.setDateDebutEncheres(LocalDateTime.now()); }
        if (article.getDateFinEncheres() == null) {
            errors.append("Champs obligatoire. L'article n'a pas de date de fin d'enchère.").append("\n");
        }
        LocalDateTime dateFinEncheres = article.getDateFinEncheres();
        if (dateFinEncheres.isBefore(LocalDateTime.now()) || dateFinEncheres.isBefore(article.getDateDebutEncheres())) {
            errors.append("Champs incorrecte. La date de fin d'enchère est invalide.").append("\n");
        }
        if (article.getVendeur() == null) {
            errors.append("Champs obligatoire. L'article n'a pas de vendeur.").append("\n");
        }
        if (article.getCategorie() == null) {
            errors.append("Champs obligatoire. L'article n'a pas de catégorie.").append("\n");
        }
        if (new CategorieManager().getById(article.getNoCategorie()) == null) {
            errors.append("Champs incorrect. La catégorie renseignée n'existe pas.").append("\n");
        }
        if (article.getMiseAPrix() < 0) {
            errors.append("Champs incorrect. La mise à prix ne peut pas être négative.").append("\n");
        }
        if (article.getPrixVente() < 0) {
            errors.append("Champs incorrect. Le prix de vente ne peut pas être négatif").append("\n");
        }
        if (!errors.toString().isEmpty()) { throw new EException(errors.toString()); }
    }

    private boolean checkUnicity(Article article) throws EException {
        return getById(article.getNoArticle()) != null;
    }
}
