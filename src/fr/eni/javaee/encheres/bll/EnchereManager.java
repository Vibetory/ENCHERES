package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.jdbc.TransactSQLQueries;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EnchereManager extends GenericManager<Enchere> {
    private final DAO<Enchere> DAOEnchere;
    private Enchere temporaryHighestBid;

    // CONSTRUCTOR
    public EnchereManager() throws EException {
        super();
        this.DAOEnchere = DAOFactory.getEnchereDAO();
    }

    /**
     * @param identifier int | Identifier of the article.
     * @return Enchere | Enchere with the highest bid.
     * @throws EException EException | ENCHERE_GET_HIGHEST_BID
     */
    public Enchere getHighestBid(int identifier) throws EException {
        try {
            return DAOEnchere.selectBy(TransactSQLQueries.SELECT_ENCHERE_MAX(), Collections.singleton(identifier));
        } catch (EException eException) { throw new EException(CodesExceptionBLL.ENCHERE_GET_HIGHEST_BID, eException); }

    }

    /**
     * Call the getHighestBid() method with an article as argument.
     */
    public Enchere getHighestBid(Article article) throws EException {
        return getHighestBid(article.getNoArticle());
    }

    /**
     * Check if an instance of Enchere has the highest bid.
     * @param enchere Enchere
     * @return boolean | "true" if the bid is the highest.
     * @throws EException EException
     */
    public boolean isHighestBid(Enchere enchere) throws EException {
        return enchere.getMontantEnchere() == getHighestBid(enchere.getArticleVendu()).getMontantEnchere();
    }

    /**
     * @param utilisateur Utilisateur
     * @return List<Enchere> | List of all the bids made by a user.
     * @throws EException EException | ENCHERE_GET_ENCHERES_FROM
     */
    public List<Enchere> getEncheresFrom(Utilisateur utilisateur) throws EException {
        try {
            int noUtilisateur = utilisateur.getNoUtilisateur();
            return DAOEnchere.selectAllByFields(new HashMap<String, Object>() {{
                put("encherisseur", noUtilisateur);
            }});
        } catch (EException eException) { throw new EException(CodesExceptionBLL.ENCHERE_GET_ENCHERES_FROM, eException); }
    }

    /**
     * Call the getEncheresFrom() method with a given user, and filter the instances of Enchere  the user is actually winning.
     */
    public List<Enchere> getWinningEncheresFrom(Utilisateur utilisateur) throws EException {
        List<Enchere> winningEncheres = new ArrayList<>();
        for (Enchere enchere : getEncheresFrom(utilisateur)) {
            if (isHighestBid(enchere)) { winningEncheres.add(enchere); }
        }
        return winningEncheres;
    }

    /**
     * @param article Article | Article the bids to delete are on.
     * @param updateCredits boolean | "true" if the credits of the user needs to be updated after deletion.
     *                      "false" otherwise.
     *                      Must be "false" while deleting all the bids when the sale ends since the winning user must not get his/her credits back.
     * @throws EException EException | ENCHERE_DELETE_ALL_BY
     */
    public void deleteAllBy(Article article, boolean updateCredits) throws EException {
        try {
            this.temporaryHighestBid = getHighestBid(article);
            /*
            Note:
            A loop is not necessary, the table having two identifiers, and the first one being "articleVendu".
            The SQL Query will be executed with only the identifier for the article, and delete all the matching occurrences.
             */
            DAOEnchere.delete(article.getNoArticle());
            if (updateCredits) { setCredits(this.temporaryHighestBid, null); }
        } catch (EException eException) {
            throw new EException(CodesExceptionBLL.ENCHERE_DELETE_ALL_BY, eException);
        }
    }

    /**
     * Delete all the bids on a given article, updating the credits of the user.
     */
    public void deleteAllBy(Article article) throws EException {
        deleteAllBy(article, true);
    }


    /**
     * Delete all the encheres made by a user.
     * @param utilisateur Utilisateur | Utilisateur to delete the bids from.
     * @throws EException EException | ENCHERE_DELETE_ALL_BY
     */
    public void deleteAllBy(Utilisateur utilisateur) throws EException {
        try {
            List<Enchere> encheres = getEncheresFrom(utilisateur);
            /*
             Note:
             While deleting a bid from a user, the delete() method is checking if it is the highest one.
             If it is, the next one with a user having enough credits will be the new highest one.
             The other ones (before that) with not enough credits will be deleted as well.
             */
            for (Enchere enchere: encheres) { delete(enchere); }
        } catch (EException eException) {
            throw new EException(CodesExceptionBLL.ENCHERE_DELETE_ALL_BY, eException);
        }
    }

    /**
     * Delete all the bids on an article when the sale is over and set the highest one as the "acquereur" of the article.
     */
    public void deleteAllWhenOver(Article article) throws EException {
        if (article.getDateFinEncheres().isBefore(LocalDateTime.now())) {
            article.setAcquereur(getHighestBid(article).getEncherisseur());
            article.setEtatVente();
            new ArticleManager().update(article);
            deleteAllBy(article, false);
        }
    }

    @Override
    public Enchere add(Enchere enchere) throws EException {
        this.temporaryHighestBid = getHighestBid(enchere.getNoArticleVendu());
        return super.add(enchere);
    }

    @Override
    public Enchere update(Enchere enchere) throws EException {
        this.temporaryHighestBid = getHighestBid(enchere.getNoArticleVendu());
        return super.update(enchere);
    }


    // LOGIC  & CHECKS

    /**
     * Get an array of the identifiers values for a given Enchere.
     */
    @Override
    protected int[] getIdentifiers(Enchere enchere) {
        return new int[] {enchere.getNoArticleVendu(), enchere.getNoEncherisseur()};
    }

    @Override
    protected void executeUpdate(Enchere enchere, String operationCRUD) throws EException {
        if (!operationCRUD.equals("DELETE")) { setCredits(this.temporaryHighestBid, enchere); }
        /*
         If the highest bid is deleted, the credits for the next highest one are adjusted.
         If the credits are not enough, that bid is also deleted, and the operation is repeated on the next one until one with enough credits is found.
         */
        else if (enchere.getMontantEnchere() == this.temporaryHighestBid.getMontantEnchere()) {
            setCredits(null, getHighestBid(enchere.getArticleVendu()));
        }
    }

    protected void checkAttributes(Enchere enchere) throws EException {
        if (enchere == null) { throw new EException(CodesExceptionBLL.BO_NULL_ERROR.get("Enchere")); }
        StringBuilder errors = new StringBuilder();
        if (enchere.getArticleVendu() == null) {
            errors.append("Champs obligatoire. L'enchère n'a pas d'article associé.").append("\n");
        }
        if (enchere.getEncherisseur() == null) {
            errors.append("Champs obligatoire. L'enchère n'a pas d'utilisateur associé.").append("\n");
        }
        if (enchere.getDateEnchere() == null) {
            errors.append("Champs obligatoire. L'enchère n'a pas de date associée").append("\n");
        }
        if (enchere.getDateEnchere().isAfter(enchere.getArticleVendu().getDateFinEncheres())) {
            errors.append("Champs incorrect. L'enchère sur l'article associée est déjà terminée.").append("\n");
        }
        if (this.temporaryHighestBid != null && enchere.getMontantEnchere() > this.temporaryHighestBid.getMontantEnchere()) {
            errors.append("Champs incorrect. Le montant de l'enchère doit être supérieur à l'enchère actuelle la plus haute.").append("\n");
        }
        if (enchere.getMontantEnchere() > enchere.getEncherisseur().getCredits()) {
            errors.append("Champs incorrect. L'utilisateur ne dispose pas de suffisamment de crédits.").append("\n");
        }
        if (!errors.toString().isEmpty()) { throw new EException(errors.toString()); }
    }

    protected boolean checkUnity(Enchere enchere) throws EException {
        return getById(enchere.getNoArticleVendu(), enchere.getNoEncherisseur()) != null;
    }

    /**
     * Adjust and update the users credits.
     * @param encherePlus Enchere | Enchere of the user the credits will to be added to.
     * @param enchereMinus Enchere | Enchere of the user the credits will to be subtracted from.
     * @throws EException EException
     */
    private void setCredits(Enchere encherePlus, Enchere enchereMinus) throws EException {
        UtilisateurManager utilisateurManager = new UtilisateurManager();
        if (encherePlus != null) {
            encherePlus.getEncherisseur().addCredits(encherePlus.getMontantEnchere());
            utilisateurManager.update(encherePlus.getEncherisseur());
        }
        if (enchereMinus != null && enchereMinus.getEncherisseur().getCredits() >= enchereMinus.getMontantEnchere()) {
            enchereMinus.getEncherisseur().substractCredits(enchereMinus.getMontantEnchere());
            utilisateurManager.update(enchereMinus.getEncherisseur());
        } else if (enchereMinus != null) { delete(enchereMinus); }
    }
}
