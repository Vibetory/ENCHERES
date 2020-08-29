package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.jdbc.TransactSQLQueries;

import java.util.Collections;

public class EnchereManager extends GenericManager<Enchere> {
    private final DAO<Enchere> DAOEnchere;

    public EnchereManager() throws EException {
        super();
        this.DAOEnchere = DAOFactory.getEnchereDAO();
    }

    @Override
    protected int[] executeLogic(Enchere enchere, String operationCRUD) throws EException {
        boolean alreadyExists = checkUnicity(enchere);
        Enchere highestBid;
        if (operationCRUD.equals("INSERT") && alreadyExists) { throw new EException(CodesExceptionBLL.ADD_ALREADY_EXIST_ERROR.get("Enchere")); }
        if (operationCRUD.equals("UPDATE") && !alreadyExists) { throw new EException(CodesExceptionBLL.UPDATE_NOT_EXIST_ERROR.get("Enchere")); }
        if (operationCRUD.equals("DELETE") && !alreadyExists) { throw new EException(CodesExceptionBLL.DELETE_NOT_EXIST_ERROR.get("Enchere")); }
        else { highestBid = checkAttributes(enchere); }
        setCredits(highestBid, enchere);
        return new int[] {enchere.getNoArticleVendu(), enchere.getNoEncherisseur()};
    }

    public Enchere getHighestBid(int identifier) throws EException {
        return DAOEnchere.selectBy(TransactSQLQueries.SELECT_ENCHERE_MAX(), Collections.singleton(identifier));
    }

    public Enchere getHighestBid(Article article) throws EException {
        return getHighestBid(article.getNoArticle());
    }

    private Enchere checkAttributes(Enchere enchere) throws EException {
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
        Enchere highestBid = getHighestBid(enchere.getArticleVendu());
        if (enchere.getMontantEnchere() > highestBid.getMontantEnchere()) {
            errors.append("Champs incorrect. Le montant de l'enchère doit être supérieur à l'enchère actuelle la plus haute.").append("\n");
        }
        if (enchere.getMontantEnchere() > enchere.getEncherisseur().getCredits()) {
            errors.append("Champs incorrect. L'utilisateur ne dispose pas de suffisamment de crédits.").append("\n");
        }
        if (!errors.toString().isEmpty()) { throw new EException(errors.toString()); }
        return highestBid;
    }

    private boolean checkUnicity(Enchere enchere) throws EException {
        return getById(enchere.getNoArticleVendu(), enchere.getNoEncherisseur()) != null;
    }

    /**
     * Adjust and update the users credits.
     * @param highestBid Enchere | Actual highest "montantEnchere".
     * @param enchere Enchere | New highest "montantEnchere".
     * @throws EException EException
     */
    private void setCredits(Enchere highestBid, Enchere enchere) throws EException {
        UtilisateurManager utilisateurManager = new UtilisateurManager();
        highestBid.getEncherisseur().addCredits(highestBid.getMontantEnchere());
        enchere.getEncherisseur().substractCredits(enchere.getMontantEnchere());
        utilisateurManager.update(highestBid.getEncherisseur());
        utilisateurManager.update(enchere.getEncherisseur());
    }
}
