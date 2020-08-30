package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Retrait;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.jdbc.TransactSQLQueries;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArticleManager extends GenericManager<Article> {
    private final DAO<Article> DAOArticle;

    // CONSTRUCTOR
    public ArticleManager() throws EException {
        super();
        this.DAOArticle = DAOFactory.getArticleDAO();
    }

    /**
     * Get an article by its identifier, and set its "etatVente".
     */
    @Override
    public Article getById(int... identifiers) throws EException {
        Article article =  super.getById(identifiers);
        article.setEtatVente();
        return article;
    }

    /**
     * @param field String | "vendeur" or "acquereur".
     * @param utilisateur Utilisateur
     * @return List<Article> | List of articles for a given user.
     * @throws EException EException | ARTICLE_GET_ALL_FROM_ERROR
     */
    private List<Article> getArticlesFrom(String field, Utilisateur utilisateur) throws EException {
        int noUtilisateur = utilisateur.getNoUtilisateur();
        try { return this.DAOArticle.selectAllByField(field, noUtilisateur); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.ARTICLE_GET_ALL_FROM_ERROR, eException);
        }
    }

    /**
     * Call getArticlesFrom with "vendeur".
     */
    public List<Article> getArticlesFromVendeur(Utilisateur vendeur) throws EException {
        return getArticlesFrom("vendeur", vendeur);
    }

    /**
     * Call getArticlesFrom with "acquereur" after updating the list of articles won by the user.
     */
    public List<Article> getArticlesFromAcquereur(Utilisateur acquereur) throws EException {
        setArticlesObtenus(acquereur);
        return getArticlesFrom("acquereur", acquereur);
    }

    /**
     * @param variable String | Search input for the name or description of a user.
     * @return List<Article> | List of articles including part of the variable in their name or description.
     * @throws EException EException |
     */
    public List<Article> getArticlesByNomOrDescription(String variable) throws EException {
        try { return DAOArticle.selectAllBy(TransactSQLQueries.SELECT_ARTICLES_LIKE(variable), null); }
        catch (EException eException) { throw new EException(CodesExceptionBLL.ARTICLE_GET_ALL_BY_NAME_DESCRIPTION, eException); }
    }

    /**
     * Update the articles won for a given user.
     */
    public void setArticlesObtenus(Utilisateur utilisateur) throws EException {
        try {
            EnchereManager enchereManager = new EnchereManager();
            for (Enchere enchere : enchereManager.getWinningEncheresFrom(utilisateur)) {
                Article article = enchere.getArticleVendu();
                String etatVente = article.getEtatVente();
                if (etatVente.equals("Enchères terminées")) {
                    article.setPrixVente(enchere.getMontantEnchere());
                    article.setAcquereur(utilisateur);
                    enchereManager.deleteAllWhenOver(article);
                }
                update(article);
            }
        } catch (EException eException) { throw new EException(CodesExceptionBLL.ARTICLE_SET_ARTICLES_WON, eException); }

    }

    /**
     * Update the articles won for all the users.
     */
    public void setAllArticlesObtenus() throws EException {
        for (Utilisateur utilisateur : new UtilisateurManager().getAll()) {
            setArticlesObtenus(utilisateur);
        }
    }

    /**
     * Set an article as having been picked up.
     */
    public void setRetraitEffectue(Article article) throws EException {
        article.setRetraitEffectue(true);
        RetraitManager retraitManager = new RetraitManager();
        Retrait retrait = retraitManager.getById(article.getNoArticle());
        retraitManager.delete(retrait);
        update(article);
    }

    /**
     * Delete all the articles from a given seller.
     */
    void deleteAllByVendeur(Utilisateur utilisateur) throws EException {
        List<Article> articles = getArticlesFromVendeur(utilisateur);
        for (Article article : articles) { delete(article); }
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

    /**
     * Get an array of the identifiers values for a given Article.
     */
    @Override
    protected int[] getIdentifiers(Article article) {
        return new int[] {article.getNoArticle()};
    }

    /**
     * Executed after the operation. Update the different related table(s).
     * >> Create a new Retrait for the given inserted article.
     * >> Clear all the Retrait and Enchere for the given deleted article.
     */
    protected void executeUpdate(Article article, String operationCRUD) throws EException {
        if (operationCRUD.equals("INSERT")) {
            Retrait retrait = new Retrait(article);
            new RetraitManager().add(retrait);
        }
        if (operationCRUD.equals("DELETE")) {
            RetraitManager retraitManager = new RetraitManager();
            Retrait retrait = retraitManager.getById(article.getNoArticle());
            retraitManager.delete(retrait);
            new EnchereManager().deleteAllBy(article);
        }
    }

    /**
     * Check all the attributes of an article.
     * @param article Article | Article to check.
     * @throws EException EException | Newly created exception.
     */
    protected void checkAttributes(Article article) throws EException {
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

    /**
     * Check if an article already exists in the database.
     */
    protected boolean checkUnicity(Article article) throws EException {
        Map<String, Object> fields = new HashMap<String, Object>() {{
            put("nomArticle", article.getNomArticle());
            put("description", article.getDescription());
        }};
        return DAOArticle.selectByFields(fields) != null;
    }
}
